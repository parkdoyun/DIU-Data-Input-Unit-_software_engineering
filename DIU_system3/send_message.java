package DIU_system3;
import GenCol.*;

// 소프트웨어공학 DIU 과제
// 12161569 컴퓨터공학과 박도윤

// input으로 들어오는 data 형식
// request하는 data는 type을 "재요청"으로 설정하고, address, region, date, energy는 비운 형태로 사용

// 회사 AMI에서 전송중단/재개 보낼 때 objectID, messageID, DIU_ID, type 제외하고 전부 비움

public class send_message extends entity {
	public int objectID; // 전력 객체 ID, [회사 AMI에서 전송 시 AMI_ID]
	public int messageID; // [회사 AMI에서 전송 시 DIU_ID]
	public int DIU_ID; // DIU_ID, [회사 AMI에서 전송 시 전송 중단/재개 플래그]
	public String type; // 종류, [회사 AMI에서 전송 시 "company_stop"]
	public String address; // 주소
	public String region; // 지역
	public String date; // 날짜
	public int energy; // 1일 사용전력량
	
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
