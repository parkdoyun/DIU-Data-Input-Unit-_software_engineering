package DIU_system3;
import genDevs.modeling.*;
import simView.ViewableAtomic;

// 소프트웨어공학 DIU 과제
// 12161569 컴퓨터공학과 박도윤
// DIU로부터 회사 AMI로부터의 전송이 재개됐다고 신호 받으면
// 10의 시간마다 DIU의 저장장치에 남은 데이터를 회사 AMI로 전송할 신호 전송
// DIU에서 신호를 받으면 저장장치 확인
// 저장장치가 비었으면 send_cnt로 전송 중단 신호를 보내고
// 저장장치에 데이터가 있으면 10의 시간마다 회사 AMI로 데이터 전송

public class send_cnt extends ViewableAtomic{
	
	String name;
	protected double processing_time;
	
	protected out_message o_m; // 전송 재개됐다고 알림
	
	public send_cnt()
	{
		this("send_cnt", 10);
	}
	public send_cnt(String _name, double Processing_time)
	{
		super(_name);
		name = _name;
		
		addInport("in"); // 전송 재개됐는지 신호 받을 포트
		addOutport("out"); // 10마다 신호 전송 -> 회사 AMI에 보내도록
		
		processing_time = Processing_time;
	}
	public void initialize()
	{
		o_m = new out_message("0", 0, 0, "", "", "", "", 0);
		holdIn("STOP", processing_time);
	}
	public void deltext(double e, message x)
	{
		Continue(e);
		for(int i = 0; i < x.getLength(); i++)
		{
			o_m = (out_message)x.getValOnPort("in", i); // output data 받음
			// 어떤 것 받았는지 0 -> 전송 중단, 1 -> 전송 재개, 2-> 저장장치에 데이터 없음
			if(o_m.messageID == 1) // 전송 재개
			{
				System.out.println("SEND_CNT -> 회사 AMI로 전송 재개");
				holdIn("START", processing_time); // 전송 재개 10
			}
			else if(o_m.messageID == 0) // 전송 중단
			{
				System.out.println("SEND_CNT -> 회사 AMI로 전송 중단");
				holdIn("STOP", INFINITY); // 전송 중단
			}
			else // 저장장치에 데이터 존재하지 않음
			{
				System.out.println("SEND_CNT -> 저장장치에 남은 데이터 없음");
				holdIn("STOP", INFINITY); 
			}
		}
		
	}
	public void deltint()
	{
		if(phaseIs("START"))
		{
			o_m = new out_message("0", 0, 0, "", "", "", "", 0);
			holdIn("START", processing_time);
		}
		
	}
	public message out()
	{
		message m = new message();
		if (phaseIs("START"))
		{
			String str = Integer.toString(1);
			m.add(makeContent("out", new send_message(str, 0, 0, "send_signal", "", "", "", 0)));
			
		}
		return m;
	}

}
