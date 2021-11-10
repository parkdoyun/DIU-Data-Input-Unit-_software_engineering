package DIU_system;
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

	public AMI()
	{
		this("proc", 20);
	}

	public AMI(String name, double Processing_time)
	{
		super(name);
		AMI_name = name;
    
		addInport("in");
		//addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		o_m = new out_message("0", 0, 0, "", "", "", "", 0);
		
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
					System.out.println(AMI_name + " arrived."); // 몇번 AMI에 도착했는지 콘솔에 출력
					
					holdIn("busy", processing_time);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			o_m = new out_message("0", 0, 0, "", "", "", "", 0);
			
			holdIn("wait", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			m.add(makeContent("out", o_m));
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

