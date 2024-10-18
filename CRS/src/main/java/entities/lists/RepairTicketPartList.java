package entities.lists;

import entities.RepairTicketPart;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "repairTicketParts")
public class RepairTicketPartList {
    private List<RepairTicketPart> repairTicketPartList;

    @XmlElement(name = "repairTicketPart")
    public List<RepairTicketPart> getRepairTicketPartList() {
        return this.repairTicketPartList;
    }

    public void setRepairTicketPartList(List<RepairTicketPart> repairTicketPartList) {
        this.repairTicketPartList = repairTicketPartList;
    }
}
