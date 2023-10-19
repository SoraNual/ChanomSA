package ku.cs.models;

public class Member {
    private int member_id;
    private String member_name;
    private String member_phone_number;

    public Member(int member_id, String member_name, String member_phone_number) {
        this.member_id = member_id;
        this.member_name = member_name;
        this.member_phone_number = member_phone_number;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_phone_number() {
        return member_phone_number;
    }

    public void setMember_phone_number(String member_phone_number) {
        this.member_phone_number = member_phone_number;
    }
}
