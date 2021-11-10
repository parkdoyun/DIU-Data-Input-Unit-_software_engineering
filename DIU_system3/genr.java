package DIU_system3;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;
import java.util.Random;
import java.time.LocalDate;

// 소프트웨어공학 DIU 과제
// 12161569 컴퓨터공학과 박도윤

public class genr extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
	
	protected send_message r_m; // request로 들어오는 data
	
	int request_cnt = 0; // 1이면 request 해야함 (재요청 data 입력으로 들어왔는지 확인하는 변수)
	
	int object_id_cnt = 0; // object ID 보낼 때마다 1씩 증가시키기 위한 변수 ex) 10001, 10002, 10003, ...
	int message_id_cnt = 0; // message ID 보낼 때마다 1씩 증가시키기 위한 변수 ex) 100001, 100002, 100003, ...
	int DIU_ID_cnt = 0; // 1 더하고 (% 9000) -> 1000 + (0 ~ 8999), DIU_ID 무조건 1000~9999 사이에 있도록 설정
	
  
	public genr() 
	{
		this("genr", 30);
		
	}
  
	public genr(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		r_m = new send_message("0", 0, 0, "", "", "", "", 0);
		count = 1;
		
		holdIn("read_arr", int_arr_time);
		
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("read_arr"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					r_m = (send_message)x.getValOnPort("in", i); // 재요청 data
					request_cnt = 1;
					
					// 콘솔에 request 정보 출력
					System.out.println("=========REQUEST DATA[GENERATOR]==========");
					System.out.println(r_m.objectID + " " + r_m.messageID + " " + r_m.DIU_ID + " " + r_m.type);
					System.out.println("==========================================");
				
					//holdIn("request", INFINITY);
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("active"))
		{
			r_m = new send_message("0", 0, 0, "", "", "", "", 0);
			count = count + 1;
			
			holdIn("active", int_arr_time);
		}
	}

	public message out()
	{
		message m = new message(); // 메시지 생성
		
		if(request_cnt == 0)
		{
			// 정보 생성
			int o_ = 10001 + object_id_cnt; // object ID
			object_id_cnt++;
			String o_str = Integer.toString(o_);
			int _m = 100001 + message_id_cnt; // message ID
			message_id_cnt++;
			int _d = 1000 + DIU_ID_cnt; // DIU ID
			DIU_ID_cnt = (DIU_ID_cnt + 1) % 9000;
			
			// 처음 10개 전부 회사 AMI로 보냄 (0 ~ 9)
			Random random = new Random();
			int rnd_n = random.nextInt(3); // 0 ~ 2 랜덤 생성
			String t; // type
			if(object_id_cnt <= 10) t = "회사";
			else
			{
				// 0 : 주택, 1 : 회사, 2 : 학교
				// 0 : 용현1동, 1 : 용현2동, 2 : 용현3동
				if(rnd_n == 0) t = "주택";
				else if(rnd_n == 1) t = "학교";
				else t = "회사";	
			}
			
			rnd_n = random.nextInt(3);
			String r; // region
			if(rnd_n == 0) r = "용현1동";
			else if(rnd_n == 1) r = "용현2동";
			else r = "용현3동";
			
			String a = "인천시 미추홀구"; // 임의 주소 (address)
			
			LocalDate now = LocalDate.now(); // 현재 시간 (date)
			String _date = now.getYear() + "년 " + now.getMonthValue() + "월 "
					+ now.getDayOfMonth() + "일"; 
			
			rnd_n = random.nextInt(10);
			rnd_n++; // 1 ~ 10
			int e = 1000 * rnd_n; // energy
			
			rnd_n = random.nextInt(2);
			
			// rnd_n이 0이면 오류 X, 1이면 오류 O
			if(rnd_n == 1)
			{
				t = ""; // 오류 생성 (빈 content 생성)
			}
			
			// 전송
			m.add(makeContent("out", new send_message(o_str, _m, _d, t, a, r, _date, e)));
			
			
		}
		else // 재전송 (request)
		{
			// 정보 생성
			int o_ = r_m.objectID; // object ID (request로 온 data 그대로 사용)
			String o_str = Integer.toString(o_);
			
			int _m = r_m.messageID; // message ID
			int _d = r_m.DIU_ID; // DIU ID		
			
			// 처음 10개 전부 회사 AMI로 보냄 (0 ~ 9)
			Random random = new Random();
			int rnd_n = random.nextInt(3); // 0 ~ 2 랜덤 생성
			String t; // type
			if(o_ <= 10010) t = "회사";
			else
			{
				// 0 : 주택, 1 : 회사, 2 : 학교
				// 0 : 용현1동, 1 : 용현2동, 2 : 용현3동
				if(rnd_n == 0) t = "주택";
				else if(rnd_n == 1) t = "학교";
				else t = "회사";	
			}
			
			rnd_n = random.nextInt(3);
			String r; // region
			if(rnd_n == 0) r = "용현1동";
			else if(rnd_n == 1) r = "용현2동";
			else r = "용현3동";
			
			String a = "인천시 미추홀구"; // 임의 주소 (address)
			
			LocalDate now = LocalDate.now(); // 현재 시간 (date)
			String _date = now.getYear() + "년 " + now.getMonthValue() + "월 "
					+ now.getDayOfMonth() + "일"; 
			
			rnd_n = random.nextInt(10);
			rnd_n++; // 1 ~ 10
			int e = 1000 * rnd_n; // energy
			
			// 전송
			m.add(makeContent("out", new send_message(o_str, _m, _d, t, a, r, _date, e)));
			
			
			request_cnt = 0; // request 표시하는 변수 초기화
		}				
		
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

}
