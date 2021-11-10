package DIU_system;
import GenCol.*;

// ����Ʈ������� DIU ����
// 12161569 ��ǻ�Ͱ��а� �ڵ���

// input���� ������ data ����
// request�ϴ� data�� type�� "���û"���� �����ϰ�, address, region, date, energy�� ��� ���·� ���

public class send_message extends entity {
	public int objectID; // ���� ��ü ID
	public int messageID;
	public int DIU_ID;
	public String type; // ����
	public String address; // �ּ�
	public String region; // ����
	public String date; // ��¥
	public int energy; // 1�� ������·�
	
	public send_message(String o, int m, int d, String t, String a, String r, String _date, int e)
	{		
		super(o);
		
		int _o = Integer.parseInt(o); // string to int
		
		this.objectID = _o;
		this.messageID = m;
		this.DIU_ID = d;
		this.type = t;
		this.address = a;
		this.region = r;
		this.date = _date;
		this.energy = e;		
	}
	
}
