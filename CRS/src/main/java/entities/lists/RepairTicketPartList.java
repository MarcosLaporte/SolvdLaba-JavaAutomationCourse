package entities.lists;

import entities.RepairTicketPart;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "repairTicketParts")
public class RepairTicketPartList {
    private List<RepairTicketPart> repairTicketParts;

    @XmlElement(name = "repairTicketPart")
    public List<RepairTicketPart> getRepairTicketParts() {
        return this.repairTicketParts;
    }

    public void setRepairTicketParts(List<RepairTicketPart> repairTicketParts) {
        this.repairTicketParts = repairTicketParts;
    }
}
