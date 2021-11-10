package DIU_system3;
import genDevs.modeling.*;
import java.util.LinkedList;
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
	
	protected send_message c_m; // ȸ�� AMI�κ��� ������ ���� ���� message
	protected int company_stop_chk = 1; // 0�̸� ���� �ߴ�, 1�̸� ���� �簳
	
	protected LinkedList<out_message> queue = new LinkedList<out_message>(); // ���� �ߴ� ���� ȸ��AMI�� ���� data ������ġ
	
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
		addOutport("send_check"); // ���� �ߴ�/�簳 �Ǿ��ٰ� ��ȣ ������ ��Ʈ
		
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
					s_m = (send_message)x.getValOnPort("in", i); // input data ����
					
					if(s_m.type == "company_stop") // ȸ�� AMI�κ��� message �޾��� ��
					{
						company_stop_chk = s_m.DIU_ID;
						if(company_stop_chk == 0)
						{
							System.out.println("DIU -> ȸ�� AMI ���� �ߴ� signal ����");
							holdIn("wait_signal", 0); // send_cnt�� �ߴ� ��ȣ �ٷ� ����
						}
						else
						{
							System.out.println("DIU -> ȸ�� AMI ���� �簳 signal ����");
							holdIn("send_signal", 0); // ���� �簳 -> queue�� �ִ� ���� �۽�
						}
						
						
					}
					else if(s_m.type == "send_signal") // ȸ�� AMI�� 10���� �����϶�� ��ȣ ����
					{
						holdIn("queue_send", 0); // ������ġ�� �ִ� �� ȸ�� AMI�� �ٷ� ����
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
				else
				{ // ���� �簳 ī��Ʈ�� 1�϶��� ����, 0�϶��� queue�� ����
					if(company_stop_chk == 1) m.add(makeContent("out_ami_company", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // ȸ��
					else queue.addLast(new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy));
				}
				
				// ������ ���� (����1��, ����2��, ����3��)
				if(ami_id % 10 == 1) m.add(makeContent("out_ami_yh1", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // ����1��				
				else if(ami_id % 10 == 2) m.add(makeContent("out_ami_yh2", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // ����2��				
				else m.add(makeContent("out_ami_yh3", new out_message(d_str, s_m.messageID, ami_id, s_m.type, s_m.address, s_m.region, s_m.date, s_m.energy))); // ����3��				
				
				System.out.println("===========================================");
			
			}
			
		}
		if (phaseIs("queue_send")) // ������ġ�� �ִ� ������ ����
		{
			if(queue.size() >= 1) // ������ġ�� �����Ͱ� �ִٸ�
			{
				out_message send_o_m = (out_message)queue.getFirst();
				queue.removeFirst(); // ���� ������ ���� ��ġ���� ����
				m.add(makeContent("out_ami_company", send_o_m)); // ȸ��AMI�� ����
				System.out.println("==============QUEUE SEND[DIU]=============="); // ���
				System.out.println(send_o_m.DIU_ID + ' ' + send_o_m.messageID + ' ' + send_o_m.AMI_ID + ' ' + send_o_m.type + ' ' + send_o_m.address + ' ' +
						send_o_m.region + ' ' + send_o_m.date + ' ' + send_o_m.energy);
				System.out.println("===========================================");
				
			}
			else // ������� send_cnt�� ���� �ߴ� ��ȣ ����
			{
				m.add(makeContent("send_check", new out_message("0", 2, 0, "", "", "", "", 0))); // �ߴ� ��ȣ ����
				System.out.println("DIU -> QUEUE IS EMPTY");
			}
			
			holdIn("wait", INFINITY);
		}
		if (phaseIs("send_signal")) // send_cnt�� �簳 ��ȣ ����
		{
			m.add(makeContent("send_check", new out_message("0", 1, 0, "", "", "", "", 0)));
			holdIn("wait", INFINITY);
		}
		if (phaseIs("wait_signal")) // send_cnt�� �ߴ� ��ȣ ����
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

