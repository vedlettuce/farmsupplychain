package Farmsupplychain;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.util.Objects;
 
@DataType()
public final class Produce {
 
	@Property()
	private final String produceId;
	 
	@Property()
	private final String produceDescription;
	 
	@Property()
	private final String producerName;
	 
	@Property()
	private final String producerAddress;
	 
	@Property()
	private final String harvestDate;
	
	@Property()
	private final String distributorName;
	
	@Property()
	private final String distributorAddress;
	
	@Property()
	private final String prodToDistDate;

	@Property()
	private final String retailerName;
	
	@Property()
	private final String retailerAddress;
	
	@Property()
	private final String distToRetaDate;

	public String getProduceId() {
		return produceId;
	}
	 
	public String getProduceDescription() {
		return produceDescription;
	}
	
	public String getProducerName() {
		return producerName;
	}
	
	public String getProducerAddress() {
		return producerAddress;
	}
	
	public String getHarvestDate() {
		return harvestDate;
	}
	
	public String getDistributorName() {
		return distributorName;
	}
	
	public String getDistributorAddress() {
		return distributorAddress;
	}
	
	public String getDistributorTransferDate() {
		return prodToDistDate;
	}
	
	public String getRetailerName() {
		return retailerName;
	}
	
	public String getRetailerAddress() {
		return retailerAddress;
	}
	
	public String getRetailerTransferDate() {
		return distToRetaDate;
	}
	
	public Produce(@JsonProperty("produceId") final String produceId,
		@JsonProperty("produceDescription") final String produceDescription,
		@JsonProperty("producerName") final String producerName,
		@JsonProperty("producerAddress") final String producerAddress,
		@JsonProperty("harvestDate") final String harvestDate,
		@JsonProperty("distributorName") final String distributorName,
		@JsonProperty("distributorAddress") final String distributorAddress,
		@JsonProperty("prodToDistDate") final String prodToDistDate,
		@JsonProperty("retailerName") final String retailerName,
		@JsonProperty("retailerAddress") final String retailerAddress,
		@JsonProperty("distToRetaDate") final String distToRetaDate
	) {
		this.produceId = produceId;
		this.produceDescription = produceDescription;
		this.producerName = producerName;
		this.producerAddress = producerAddress;
		this.harvestDate = harvestDate;
		this.distributorName = distributorName;
		this.distributorAddress = distributorAddress;
		this.prodToDistDate = prodToDistDate;
		this.retailerName = retailerName;
		this.retailerAddress = retailerAddress;
		this.distToRetaDate = distToRetaDate;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		 
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		 
		Produce other = (Produce) obj;
		 
		return Objects.deepEquals(new String[] { 
				getProduceId(), getProduceDescription(), getProducerName(),
		getProducerAddress(), getHarvestDate() },
		new String[] { other.getProduceId(), other.getProduceDescription(), other.getProducerName(),
		other.getProducerAddress(), other.getHarvestDate() }
		);
	}
	 
	@Override
	public int hashCode() {
		return Objects.hash(getProduceId(), getProduceDescription(), getProducerName(),
		getProducerAddress(), getHarvestDate());
	}
	 
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) +
		" [produceId=" + produceId +
		", produceDescription=" + produceDescription +
		", producerName=" + producerName +
		", producerAddress=" + producerAddress +
		", harvestDate=" + harvestDate +
		"]";
	}
}