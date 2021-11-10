package DIU_system3;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

// 소프트웨어공학 DIU 과제
// 12161569 컴퓨터공학과 박도윤

// data 받은 뒤에 콘솔에 출력

public class AMI extends ViewableAtomic
{
  
	protected out_message o_m;
	protected double processing_time;
	String AMI_name; 
	int message_num = 0;
	String AMI_ID_str;
	

	protected entity predict_input;
	
	public AMI()
	{
		this("AMI", 20);
	}

	public AMI(String name, double Processing_time)
	{
		super(name);
		AMI_name = name;
    
		addInport("in");
		addOutport("out");
		
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		o_m = new out_message("0", 0, 0, "", "", "", "", 0);
		//c_m = new company_message("0", 0, 0);
		
		holdIn("wait", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					o_m = (out_message)x.getValOnPort("in", i); // output data 받음
					
					
					// 정보 콘솔에 출력
					// System.out.println(AMI_name + " arrived."); // 몇번 AMI에 도착했는지 콘솔에 출력
					
					if(AMI_name == "ami_1") // 회사 AMI일 경우
					{
						message_num++; // 받은 데이터 개수
						System.out.println("COMPANY_AMI -> " + message_num + " messages.");
						if(message_num == 3) // message 3개면
						{
							AMI_ID_str = Integer.toString(o_m.AMI_ID);					
							message_num = 0; // 0으로 초기화
							holdIn("sending", 0);
						}
						else holdIn("processing", 0);
					}
					else holdIn("processing", 0);
														
				}
				
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("processing"))
		{
			o_m = new out_message("0", 0, 0, "", "", "", "", 0);
			//c_m = new company_message("0", 0, 0);
			
			holdIn("wait", INFINITY);
		}
		if (phaseIs("sending") && AMI_name == "ami_1")
		{
			holdIn("prediction", 50); // 50 시간 후 전송 재개 신호 전송
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("sending") && AMI_name == "ami_1")
		{
			// 전송 중단 DIU 전송
			m.add(makeContent("out", new send_message(AMI_ID_str,o_m.DIU_ID ,0, "company_stop", "", "", "", 0)));
			
			System.out.println("회사 AMI -> 전력 예측 시작");
		}
		
		// 전송 재개 신호 전송
		if(phaseIs("prediction") && AMI_name == "ami_1")
		{
			System.out.println("회사 AMI -> 전력 예측 완료");
			m.add(makeContent("out", new send_message(AMI_ID_str,o_m.DIU_ID ,1, "company_stop", "", "", "", 0)));			
			
			holdIn("wait", INFINITY);
		}
		
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + o_m.getName();
	}

}

