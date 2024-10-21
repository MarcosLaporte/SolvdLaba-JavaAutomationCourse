package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.RepairTicket;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "repairTickets")
public class RepairTicketList {
    @JsonProperty("repairTickets")
    private List<RepairTicket> repairTicketList;

    @XmlElement(name = "repairTicket")
    public List<RepairTicket> getRepairTicketList() {
        return this.repairTicketList;
    }

    public void setRepairTicketList(List<RepairTicket> repairTicketList) {
        this.repairTicketList = repairTicketList;
    }
}
