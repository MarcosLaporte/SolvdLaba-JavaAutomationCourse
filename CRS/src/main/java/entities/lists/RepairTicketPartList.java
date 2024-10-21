package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.RepairTicketPart;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "repairTicketParts")
public class RepairTicketPartList {
    @JsonProperty("repairTicketParts")
    private List<RepairTicketPart> repairTicketPartList;

    @XmlElement(name = "repairTicketPart")
    public List<RepairTicketPart> getRepairTicketPartList() {
        return this.repairTicketPartList;
    }

    public void setRepairTicketPartList(List<RepairTicketPart> repairTicketPartList) {
        this.repairTicketPartList = repairTicketPartList;
    }
}
