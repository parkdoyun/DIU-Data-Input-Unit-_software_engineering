package DIU_system3;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

// ����Ʈ������� DIU ����
// 12161569 ��ǻ�Ͱ��а� �ڵ���

// data ���� �ڿ� �ֿܼ� ���

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
					o_m = (out_message)x.getValOnPort("in", i); // output data ����
					
					
					// ���� �ֿܼ� ���
					// System.out.println(AMI_name + " arrived."); // ��� AMI�� �����ߴ��� �ֿܼ� ���
					
					if(AMI_name == "ami_1") // ȸ�� AMI�� ���
					{
						message_num++; // ���� ������ ����
						System.out.println("COMPANY_AMI -> " + message_num + " messages.");
						if(message_num == 3) // message 3����
						{
							AMI_ID_str = Integer.toString(o_m.AMI_ID);					
							message_num = 0; // 0���� �ʱ�ȭ
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
			holdIn("prediction", 50); // 50 �ð� �� ���� �簳 ��ȣ ����
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("sending") && AMI_name == "ami_1")
		{
			// ���� �ߴ� DIU ����
			m.add(makeContent("out", new send_message(AMI_ID_str,o_m.DIU_ID ,0, "company_stop", "", "", "", 0)));
			
			System.out.println("ȸ�� AMI -> ���� ���� ����");
		}
		
		// ���� �簳 ��ȣ ����
		if(phaseIs("prediction") && AMI_name == "ami_1")
		{
			System.out.println("ȸ�� AMI -> ���� ���� �Ϸ�");
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

