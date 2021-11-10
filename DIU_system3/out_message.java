package DIU_system3;
import GenCol.*;

// ����Ʈ������� DIU ����
// 12161569 ��ǻ�Ͱ��а� �ڵ���

// output���� ������ data ���� (AMI��)

public class out_message extends entity{

	public int DIU_ID;
	public int messageID; // [send_cnt������ ���� �ߴ�/�簳 �÷��� ��ȣ�� ���]
	public int AMI_ID;
	public String type; // ����
	public String address; // �ּ�
	public String region; // ����
	public String date; // ��¥
	public int energy; // 1�� ������·�
	
	public out_message(String d, int m, int a, String t, String _a, String r, String _date, int e)
	{
		super(d);
		
		int _d = Integer.parseInt(d);
		this.DIU_ID = _d;
		this.messageID = m;
		this.AMI_ID = a;
		this.type = t;
		this.address = _a;
		this.region = r;
		this.date = _date;
		this.energy = e;
	}
	
	public void setInfo(send_message s_m, int a)
	{
		this.DIU_ID = s_m.DIU_ID;
		this.messageID = s_m.messageID;
		this.AMI_ID = a;
		this.type = s_m.type;
		this.address = s_m.address;
		this.region = s_m.region;
		this.date = s_m.date;
		this.energy = s_m.energy;
	}
}
