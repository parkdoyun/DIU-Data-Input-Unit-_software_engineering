package DIU_system3;
import GenCol.*;

// 소프트웨어공학 DIU 과제
// 12161569 컴퓨터공학과 박도윤

// output으로 보내는 data 형식 (AMI로)

public class out_message extends entity{

	public int DIU_ID;
	public int messageID; // [send_cnt에서는 전송 중단/재개 플래그 신호로 사용]
	public int AMI_ID;
	public String type; // 종류
	public String address; // 주소
	public String region; // 지역
	public String date; // 날짜
	public int energy; // 1일 사용전력량
	
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
