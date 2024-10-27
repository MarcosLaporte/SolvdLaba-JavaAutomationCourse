package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.RepairTicketPart;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "repairTicketParts")
public class RepairTicketPartList implements IEntityList<RepairTicketPart> {
    @JsonProperty("repairTicketParts")
    private List<RepairTicketPart> repairTicketPartList;

    private RepairTicketPartList() {
    }

    private RepairTicketPartList(List<RepairTicketPart> repairTicketPartList) {
        this.repairTicketPartList = repairTicketPartList;
    }

    @XmlElement(name = "repairTicketPart")
    @Override
    public List<RepairTicketPart> getList() {
        return this.repairTicketPartList;
    }

    @Override
    public void setList(List<RepairTicketPart> repairTicketPartList) {
        this.repairTicketPartList = repairTicketPartList;
    }
}
