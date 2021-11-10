package DIU_system3;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;
import java.util.Random;
import java.time.LocalDate;

// ����Ʈ������� DIU ����
// 12161569 ��ǻ�Ͱ��а� �ڵ���

public class genr extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
	
	protected send_message r_m; // request�� ������ data
	
	int request_cnt = 0; // 1�̸� request �ؾ��� (���û data �Է����� ���Դ��� Ȯ���ϴ� ����)
	
	int object_id_cnt = 0; // object ID ���� ������ 1�� ������Ű�� ���� ���� ex) 10001, 10002, 10003, ...
	int message_id_cnt = 0; // message ID ���� ������ 1�� ������Ű�� ���� ���� ex) 100001, 100002, 100003, ...
	int DIU_ID_cnt = 0; // 1 ���ϰ� (% 9000) -> 1000 + (0 ~ 8999), DIU_ID ������ 1000~9999 ���̿� �ֵ��� ����
	
  
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
					r_m = (send_message)x.getValOnPort("in", i); // ���û data
					request_cnt = 1;
					
					// �ֿܼ� request ���� ���
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
		message m = new message(); // �޽��� ����
		
		if(request_cnt == 0)
		{
			// ���� ����
			int o_ = 10001 + object_id_cnt; // object ID
			object_id_cnt++;
			String o_str = Integer.toString(o_);
			int _m = 100001 + message_id_cnt; // message ID
			message_id_cnt++;
			int _d = 1000 + DIU_ID_cnt; // DIU ID
			DIU_ID_cnt = (DIU_ID_cnt + 1) % 9000;
			
			// ó�� 10�� ���� ȸ�� AMI�� ���� (0 ~ 9)
			Random random = new Random();
			int rnd_n = random.nextInt(3); // 0 ~ 2 ���� ����
			String t; // type
			if(object_id_cnt <= 10) t = "ȸ��";
			else
			{
				// 0 : ����, 1 : ȸ��, 2 : �б�
				// 0 : ����1��, 1 : ����2��, 2 : ����3��
				if(rnd_n == 0) t = "����";
				else if(rnd_n == 1) t = "�б�";
				else t = "ȸ��";	
			}
			
			rnd_n = random.nextInt(3);
			String r; // region
			if(rnd_n == 0) r = "����1��";
			else if(rnd_n == 1) r = "����2��";
			else r = "����3��";
			
			String a = "��õ�� ����Ȧ��"; // ���� �ּ� (address)
			
			LocalDate now = LocalDate.now(); // ���� �ð� (date)
			String _date = now.getYear() + "�� " + now.getMonthValue() + "�� "
					+ now.getDayOfMonth() + "��"; 
			
			rnd_n = random.nextInt(10);
			rnd_n++; // 1 ~ 10
			int e = 1000 * rnd_n; // energy
			
			rnd_n = random.nextInt(2);
			
			// rnd_n�� 0�̸� ���� X, 1�̸� ���� O
			if(rnd_n == 1)
			{
				t = ""; // ���� ���� (�� content ����)
			}
			
			// ����
			m.add(makeContent("out", new send_message(o_str, _m, _d, t, a, r, _date, e)));
			
			
		}
		else // ������ (request)
		{
			// ���� ����
			int o_ = r_m.objectID; // object ID (request�� �� data �״�� ���)
			String o_str = Integer.toString(o_);
			
			int _m = r_m.messageID; // message ID
			int _d = r_m.DIU_ID; // DIU ID		
			
			// ó�� 10�� ���� ȸ�� AMI�� ���� (0 ~ 9)
			Random random = new Random();
			int rnd_n = random.nextInt(3); // 0 ~ 2 ���� ����
			String t; // type
			if(o_ <= 10010) t = "ȸ��";
			else
			{
				// 0 : ����, 1 : ȸ��, 2 : �б�
				// 0 : ����1��, 1 : ����2��, 2 : ����3��
				if(rnd_n == 0) t = "����";
				else if(rnd_n == 1) t = "�б�";
				else t = "ȸ��";	
			}
			
			rnd_n = random.nextInt(3);
			String r; // region
			if(rnd_n == 0) r = "����1��";
			else if(rnd_n == 1) r = "����2��";
			else r = "����3��";
			
			String a = "��õ�� ����Ȧ��"; // ���� �ּ� (address)
			
			LocalDate now = LocalDate.now(); // ���� �ð� (date)
			String _date = now.getYear() + "�� " + now.getMonthValue() + "�� "
					+ now.getDayOfMonth() + "��"; 
			
			rnd_n = random.nextInt(10);
			rnd_n++; // 1 ~ 10
			int e = 1000 * rnd_n; // energy
			
			// ����
			m.add(makeContent("out", new send_message(o_str, _m, _d, t, a, r, _date, e)));
			
			
			request_cnt = 0; // request ǥ���ϴ� ���� �ʱ�ȭ
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
