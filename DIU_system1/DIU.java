package DIU_system;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

// ����Ʈ������� DIU ����
// 12161569 ��ǻ�Ͱ��а� �ڵ���

public class DIU extends ViewableAtomic
{
  
	protected entity job;
	protected send_message s_m; // input���� ������ data
	// request message�� type(send_message)�� "���û", ������ ���ڵ� ���
	protected double processing_time;
	
	// AMI ID
	// ���� : 1x, �б� : 2x, ȸ�� : 3x
	// ����1�� : x1, ����2�� : x2, ����3�� : x3

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
					s_m = (send_message)x.getValOnPort("in", i); // input data ����
					
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
			// ������ �з�
			// DIU_ID�� 1000 ~ 9999 ���̿� �ִ��� Ȯ�� (�´���)
			if(s_m.DIU_ID < 1000 || s_m.DIU_ID > 9999)
			{
				// ���û			
				String o_str = Integer.toString(s_m.objectID);
				m.add(makeContent("re_request", new send_message(o_str, s_m.messageID, s_m.DIU_ID, "���û", "", "", "", 0)));
				
				// �ֿܼ� request ���� ǥ��
				System.out.println("REQUEST -> " + s_m.objectID);
				holdIn("re_request", INFINITY);
			}
			else if(s_m.objectID == 0 || s_m.messageID == 0 || s_m.type == "" || s_m.address == "" 
					|| s_m.region == "" || s_m.date == "" || s_m.energy == 0) // �� content �ִ��� Ȯ��
			{
				// ���û
				String o_str = Integer.toString(s_m.objectID);
				m.add(makeContent("re_request", new send_message(o_str, s_m.messageID, s_m.DIU_ID, "���û", "", "", "", 0)));
				
				// �ֿܼ� request ���� ǥ��
				System.out.println("REQUEST -> " + s_m.objectID);
				holdIn("re_request", INFINITY);
			}
			else // ��Ȯ�� ������ -> �з� ����
			{
				// �ֿܼ� ���� ���
				holdIn("re_writing", INFINITY);
				System.out.println("==============INPUT DATA[DIU]==============");
				System.out.println(s_m.objectID + " " + s_m.messageID + " " + s_m.DIU_ID + "\n"
						+ s_m.type + ' ' + s_m.address + ' ' + s_m.region + ' ' + s_m.date + ' ' + s_m.energy + "kw");
				
				// ami id ����
				int ami_id = 0;
				// ����(����, �б�, ȸ��)�� ���� �ٸ��� ����
				if(s_m.type == "����") ami_id = 10;
				else if(s_m.type == "�б�") ami_id = 20;
				else ami_id = 30; // ȸ��
				
				// ����(����1��, ����2��, ����3��)�� ���� �ٸ��� ����
				if(s_m.region == "����1��") ami_id += 1;
				else if(s_m.region == "����2��") ami_id += 2;
				else ami_id += 3; // ����3��
				
				System.out.println("AMI ID : " + ami_id); // �ֿܼ� AMI ID ���
				
				String d_str = Integer.toString(s_m.DIU_ID);
				holdIn("sending", INFINITY);
				
				// �ش�Ǵ� AMI�� data ����
				// ������ ���� (����, �б�, ȸ��)
				if(ami_id / 10 == 1) m.add(makeContent("out_ami_house", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // ����
				else if(ami_id / 10 == 2) m.add(makeContent("out_ami_school", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // �б�				
				else m.add(makeContent("out_ami_company", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // ȸ��
				
				// ������ ���� (����1��, ����2��, ����3��)
				if(ami_id % 10 == 1) m.add(makeContent("out_ami_yh1", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // ����1��				
				else if(ami_id % 10 == 2) m.add(makeContent("out_ami_yh2", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // ����2��				
				else m.add(makeContent("out_ami_yh3", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // ����3��				
				
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

