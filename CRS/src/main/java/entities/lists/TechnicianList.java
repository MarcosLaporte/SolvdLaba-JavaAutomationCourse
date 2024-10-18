package entities.lists;

import entities.Technician;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "technicians")
public class TechnicianList {
    private List<Technician> technicianList;

    @XmlElement(name = "technician")
    public List<Technician> getTechnicianList() {
        return this.technicianList;
    }

    public void setTechnicianList(List<Technician> technicianList) {
        this.technicianList = technicianList;
    }
}
