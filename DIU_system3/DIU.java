package DIU_system3;
import genDevs.modeling.*;
import java.util.LinkedList;
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
	
	protected send_message c_m; // 회사 AMI로부터 들어오는 전송 관련 message
	protected int company_stop_chk = 1; // 0이면 전송 중단, 1이면 전송 재개
	
	protected LinkedList<out_message> queue = new LinkedList<out_message>(); // 전송 중단 동안 회사AMI로 보낼 data 저장장치
	
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
		addOutport("send_check"); // 전송 중단/재개 되었다고 신호 보내는 포트
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		//job = new entity("");
		s_m = new send_message("0", 0, 0, "", "", "", "", 0);		
		c_m = new send_message("0", 0, 0, "", "", "", "", 0);		
		
		holdIn("wait", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait") || phaseIs("sending") || phaseIs("re_request") || phaseIs("classifying"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{					
					s_m = (send_message)x.getValOnPort("in", i); // input data 받음
					
					if(s_m.type == "company_stop") // 회사 AMI로부터 message 받았을 때
					{
						company_stop_chk = s_m.DIU_ID;
						if(company_stop_chk == 0)
						{
							System.out.println("DIU -> 회사 AMI 전송 중단 signal 수신");
							holdIn("wait_signal", 0); // send_cnt에 중단 신호 바로 전송
						}
						else
						{
							System.out.println("DIU -> 회사 AMI 전송 재개 signal 수신");
							holdIn("send_signal", 0); // 전송 재개 -> queue에 있는 정보 송신
						}
						
						
					}
					else if(s_m.type == "send_signal") // 회사 AMI로 10마다 전송하라는 신호 수신
					{
						holdIn("queue_send", 0); // 저장장치에 있는 것 회사 AMI로 바로 전송
					}
					else holdIn("classifying", processing_time);
					
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
			c_m = new send_message("0", 0, 0, "", "", "", "", 0);		
			
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
				else
				{ // 전송 재개 카운트가 1일때만 전송, 0일때는 queue에 저장
					if(company_stop_chk == 1) m.add(makeContent("out_ami_company", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 회사
					else queue.addLast(new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy));
				}
				
				// 지역에 따라 (용현1동, 용현2동, 용현3동)
				if(ami_id % 10 == 1) m.add(makeContent("out_ami_yh1", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 용현1동				
				else if(ami_id % 10 == 2) m.add(makeContent("out_ami_yh2", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 용현2동				
				else m.add(makeContent("out_ami_yh3", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // 용현3동				
				
				System.out.println("===========================================");
			
			}
			
		}
		if (phaseIs("queue_send")) // 저장장치에 있는 데아터 전송
		{
			if(queue.size() >= 1) // 저장장치에 데이터가 있다면
			{
				out_message send_o_m = (out_message)queue.getFirst();
				queue.removeFirst(); // 보낼 데이터 저장 장치에서 삭제
				m.add(makeContent("out_ami_company", send_o_m)); // 회사AMI로 전송
				System.out.println("==============QUEUE SEND[DIU]=============="); // 출력
				System.out.println(send_o_m.DIU_ID + ' ' + send_o_m.messageID + ' ' + send_o_m.AMI_ID + ' ' + send_o_m.type + ' ' + send_o_m.address + ' ' +
						send_o_m.region + ' ' + send_o_m.date + ' ' + send_o_m.energy);
				System.out.println("===========================================");
				
			}
			else // 비었으면 send_cnt에 전송 중단 신호 보냄
			{
				m.add(makeContent("send_check", new out_message("0", 2, 0, "", "", "", "", 0))); // 중단 신호 전송
				System.out.println("DIU -> QUEUE IS EMPTY");
			}
			
			holdIn("wait", INFINITY);
		}
		if (phaseIs("send_signal")) // send_cnt에 재개 신호 전송
		{
			m.add(makeContent("send_check", new out_message("0", 1, 0, "", "", "", "", 0)));
			holdIn("wait", INFINITY);
		}
		if (phaseIs("wait_signal")) // send_cnt에 중단 신호 전송
		{

			m.add(makeContent("send_check", new out_message("0", 0, 0, "", "", "", "", 0)));
			holdIn("wait", INFINITY);
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

