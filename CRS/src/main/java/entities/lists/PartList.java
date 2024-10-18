package entities.lists;

import entities.Part;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "parts")
public class PartList {
    private List<Part> partList;

    @XmlElement(name = "part")
    public List<Part> getPartList() {
        return this.partList;
    }

    public void setPartList(List<Part> partList) {
        this.partList = partList;
    }
}
