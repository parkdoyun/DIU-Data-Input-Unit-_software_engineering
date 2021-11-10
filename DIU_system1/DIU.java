package DIU_system;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

// 소프트웨어공학 DIU 과제
// 12161569 컴퓨터공학과 박도윤

public class DIU extends ViewableAtomic
{
  
	protected entity job;
	protected send_message s_m; // input으로 들어오는 data
	// request message는 type(send_message)에 "재요청", 나머지 인자들 비움
	protected double processing_time;
	
	// AMI ID
	// 주택 : 1x, 학교 : 2x, 회사 : 3x
	// 용현1동 : x1, 용현2동 : x2, 용현3동 : x3

	public DIU()
	{
		this("proc", 20);
	}

	public DIU(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out_ami_company");
		addOutport("out_ami_house");
		addOutport("out_ami_school");
		addOutport("out_ami_yh1");
		addOutport("out_ami_yh2");
		addOutport("out_ami_yh3");
		addOutport("re_request");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		//job = new entity("");
		s_m = new send_message("0", 0, 0, "", "", "", "", 0);		
		
		holdIn("wait", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait") || phaseIs("sending") || phaseIs("re_request"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					//job = x.getValOnPort("in", i);
					s_m = (send_message)x.getValOnPort("in", i); // input data 받음
					
					holdIn("classifying", processing_time);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			//job = new entity("");
			s_m = new send_message("0", 0, 0, "", "", "", "", 0);		
			
			holdIn("wait", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("classifying"))
		{
			// 데이터 분류
			// DIU_ID가 1000 ~ 9999 사이에 있는지 확인 (맞는지)
			if(s_m.DIU_ID < 1000 || s_m.DIU_ID > 9999)
			{
				// 재요청			
				String o_str = Integer.toString(s_m.objectID);
				m.add(makeContent("re_request", new send_message(o_str, s_m.messageID, s_m.DIU_ID, "재요청", "", "", "", 0)));
				
				// 콘솔에 request 정보 표시
				System.out.println("REQUEST -> " + s_m.objectID);
				holdIn("re_request", INFINITY);
			}
			else if(s_m.objectID == 0 || s_m.messageID == 0 || s_m.type == "" || s_m.address == "" 
					|| s_m.region == "" || s_m.date == "" || s_m.energy == 0) // 빈 content 있는지 확인
			{
				// 재요청
				String o_str = Integer.toString(s_m.objectID);
				m.add(makeContent("re_request", new send_message(o_str, s_m.messageID, s_m.DIU_ID, "재요청", "", "", "", 0)));
				
				// 콘솔에 request 정보 표시
				System.out.println("REQUEST -> " + s_m.objectID);
				holdIn("re_request", INFINITY);
			}
			else // 정확한 데이터 -> 분류 진행
			{
				// 콘솔에 정보 출력
				holdIn("re_writing", INFINITY);
				System.out.println("==============INPUT DATA[DIU]==============");
				System.out.println(s_m.objectID + " " + s_m.messageID + " " + s_m.DIU_ID + "\n"
						+ s_m.type + ' ' + s_m.address + ' ' + s_m.region + ' ' + s_m.date + ' ' + s_m.energy + "kw");
				
				// ami id 생성
				int ami_id = 0;
				// 유형(주택, 학교, 회사)에 따라 다르게 설정
				if(s_m.type == "주택") ami_id = 10;
				else if(s_m.type == "학교") ami_id = 20;
				else ami_id = 30; // 회사
				
				// 지역(용현1동, 용현2동, 용현3동)에 따라 다르게 설정
				if(s_m.region == "용현1동") ami_id += 1;
				else if(s_m.region == "용현2동") ami_id += 2;
				else ami_id += 3; // 용현3동
				
				System.out.println("AMI ID : " + ami_id); // 콘솔에 AMI ID 출력
				
				String d_str = Integer.toString(s_m.DIU_ID);
				holdIn("sending", INFINITY);
				
				// 해당되는 AMI로 data 전송
				// 유형에 따라 (주택, 학교, 회사)
				if(ami_id / 10 == 1) m.add(makeContent("out_ami_house", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 주택
				else if(ami_id / 10 == 2) m.add(makeContent("out_ami_school", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 학교				
				else m.add(makeContent("out_ami_company", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 회사
				
				// 지역에 따라 (용현1동, 용현2동, 용현3동)
				if(ami_id % 10 == 1) m.add(makeContent("out_ami_yh1", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 용현1동				
				else if(ami_id % 10 == 2) m.add(makeContent("out_ami_yh2", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 용현2동				
				else m.add(makeContent("out_ami_yh3", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 용현3동				
				
				System.out.println("===========================================");
			}
			
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + s_m.getName();
	}

}

