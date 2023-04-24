package Farmsupplychain;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import com.owlike.genson.Genson;
import java.time.format.*;
import java.time.*;
 
@Contract(
        name = "Farmsupplychain",
        info = @Info(
                title = "Farmsupplychain contract",
                description = "Farm supply - Produce transfer chaincode",
                version = "0.0.1"))
 
 
@Default
public final class ProduceTransfer implements ContractInterface {
 
private final Genson genson = new Genson();
private enum FarmsupplyErrors {
       Produce_NOT_FOUND,
       Produce_ALREADY_EXISTS,
       Produce_NOOWNER_SPECIFIED,
       Produce_INVALID_TRANSFER,
       Produce_INVALID_OWNER,
       Produce_INVALID_ADDRESS
   }
/**
     * Add some initial properties to the ledger
     *
     * @param ctx the transaction context
     */
    @Transaction()
    public void initLedger(final Context ctx) {
   
        ChaincodeStub stub= ctx.getStub();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDateTime now = LocalDateTime.now();
        Produce Produce = new Produce("2000", "Potato","Yukon Farms","2233 Whisper Valley",dtf.format(now),
        		"","","","","","");
       
        String ProduceState = genson.serialize(Produce);
       
        stub.putStringState("1", ProduceState);
    }
   
   
    /**
     * Add new Produce on the ledger.
     *
     * @param ctx the transaction context
     * @param id the key for the new Produce
     * @param model the model of the new Produce
     * @param ownername the owner of the new Produce
     * @param value the value of the new Produce
     * @return the created Produce
     */

    @Transaction()
    public Produce addNewProduce(final Context ctx, final String produceId, final String produceDescription,
            final String producerName, final String producerAddress) {
       
    ChaincodeStub stub = ctx.getStub();
 
        String ProduceState = stub.getStringState(produceId);
       
        if (!ProduceState.isEmpty()) {
            String errorMessage = String.format("Produce %s already exists", produceId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_ALREADY_EXISTS.toString());
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDateTime now = LocalDateTime.now();
       
        Produce Produce = new Produce(produceId,produceDescription,producerName,
        		producerAddress,dtf.format(now),"","","","","","");
       
        ProduceState = genson.serialize(Produce);
       
        stub.putStringState(produceId, ProduceState);
       
        return Produce;
    }
 
 
    /**
    * Retrieves a Produce based upon Produce Id from the ledger.
    *
    * @param ctx the transaction context
    * @param id the key
    * @return the Produce found on the ledger if there was one
    */
    @Transaction()
   public Produce queryProduceById(final Context ctx, final String produceId) {
       ChaincodeStub stub = ctx.getStub();
       String ProduceState = stub.getStringState(produceId);
 
       if (ProduceState.isEmpty()) {
           String errorMessage = String.format("Produce %s does not exist", produceId);
           System.out.println(errorMessage);
           throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_NOT_FOUND.toString());
       }
       
       Produce Produce = genson.deserialize(ProduceState, Produce.class);
       return Produce;
   }

        /**
        * Changes the owner of a Produce on the ledger to distributor.
        *
        * @param ctx the transaction context
        * @param id the key
        * @param transferTo is the new owner
        * @param transferAddress is the new address
        * @return the updated Produce id
        */
       @Transaction()
       public String transferProduceDistributor(final Context ctx, final String produceId,
        final String transferedTo, final String transferAddress ) {
           ChaincodeStub stub = ctx.getStub();
 
           String ProduceState = stub.getStringState(produceId);
 
           if (ProduceState.isEmpty()) {
               String errorMessage = String.format("Produce %s does not exist", produceId);
               System.out.println(errorMessage);
               throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_NOT_FOUND.toString());
           }
           if (transferedTo.isEmpty()) {
            String errorMessage = "Distributor name cannot be empty";
            throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_INVALID_OWNER.toString());
           }
           else if (transferAddress.isEmpty()) {
            String errorMessage = "Distributor Address cannot be empty";
            throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_INVALID_ADDRESS.toString());
           }
 
           Produce Produce = genson.deserialize(ProduceState, Produce.class);
           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
           LocalDateTime now = LocalDateTime.now();
           final String tdate = dtf.format(now);
           
           if (!Produce.getDistributorName().isEmpty()) {
	        String errorMessage = "Cannot buy from self";
	        throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_INVALID_TRANSFER.toString());
	       }
	        else if (!Produce.getRetailerName().isEmpty()) {
            String errorMessage = "Cannot buy from retailer";
            throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_INVALID_TRANSFER.toString());
           }
           
           Produce newProduce = new Produce(Produce.getProduceId(), Produce.getProduceDescription(), Produce.getProducerName(),
              Produce.getProducerAddress(),Produce.getHarvestDate(),transferedTo, 
              transferAddress,tdate,"","","");
           String newProduceState = genson.serialize(newProduce);
           
           stub.putStringState(produceId, newProduceState);
         return produceId;
       }
   
        /**
        * Changes the owner of a Produce on the ledger to retailer.
        *
        * @param ctx the transaction context
        * @param id the key
        * @param transferedTo is the new owner
        * @param transferAddress is the new address
        * @return the updated Produce id
        */
       @Transaction()
       public String transferProduceRetailer(final Context ctx, final String produceId,
        final String transferedTo, final String transferAddress ) {
           ChaincodeStub stub = ctx.getStub();
 
           String ProduceState = stub.getStringState(produceId);
 
           if (ProduceState.isEmpty()) {
               String errorMessage = String.format("Produce %s does not exist", produceId);
               System.out.println(errorMessage);
               throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_NOT_FOUND.toString());
           }
 
           if (transferedTo.isEmpty()) {
            String errorMessage = "Retailer Name cannot be empty";
            throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_INVALID_OWNER.toString());
           }
           else if (transferAddress.isEmpty()) {
            String errorMessage = "Retailer Address cannot be empty";
            throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_INVALID_ADDRESS.toString());
           }
           
           Produce Produce = genson.deserialize(ProduceState, Produce.class);
           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
           LocalDateTime now = LocalDateTime.now();
           
           if (!Produce.getRetailerName().isEmpty()) {
            String errorMessage = "Cannot buy from self";
            throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_INVALID_TRANSFER.toString());
           }
           else if (Produce.getDistributorName().isEmpty()) {
            String errorMessage = "Cannot buy from producer";
            throw new ChaincodeException(errorMessage, FarmsupplyErrors.Produce_INVALID_TRANSFER.toString());
           }

           Produce newProduce = new Produce(Produce.getProduceId(), Produce.getProduceDescription(), Produce.getProducerName(),
              Produce.getProducerAddress(),Produce.getHarvestDate(),
              Produce.getDistributorName(),Produce.getDistributorAddress(),
              Produce.getDistributorTransferDate(),transferedTo,
              transferAddress,dtf.format(now));
           String newProduceState = genson.serialize(newProduce);
           
           stub.putStringState(produceId, newProduceState);
           return produceId;
       }
}