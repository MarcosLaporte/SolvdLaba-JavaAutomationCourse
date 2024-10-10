package entities;

public class RepairTicket {
    public int ticket_id;
    public int cust_id;
    public String computer_desc;
    public String issue;
    public java.sql.Date date_submitted;
    public int status;

    public RepairTicket(int ticket_id, int cust_id, String computer_desc, String issue, java.sql.Date date_submitted, int status) {
        this.ticket_id = ticket_id;
        this.cust_id = cust_id;
        this.computer_desc = computer_desc;
        this.issue = issue;
        this.date_submitted = date_submitted;
        this.status = status;
    }

    private RepairTicket(Integer ticket_id, Integer cust_id, String computer_desc, String issue, java.sql.Date date_submitted, Integer status) {
        this(ticket_id.intValue(), cust_id.intValue(), computer_desc, issue, date_submitted, status.intValue());
    }
}


