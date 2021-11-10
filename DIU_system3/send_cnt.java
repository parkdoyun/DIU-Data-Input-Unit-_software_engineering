package DIU_system3;
import genDevs.modeling.*;
import simView.ViewableAtomic;

// ����Ʈ������� DIU ����
// 12161569 ��ǻ�Ͱ��а� �ڵ���
// DIU�κ��� ȸ�� AMI�κ����� ������ �簳�ƴٰ� ��ȣ ������
// 10�� �ð����� DIU�� ������ġ�� ���� �����͸� ȸ�� AMI�� ������ ��ȣ ����
// DIU���� ��ȣ�� ������ ������ġ Ȯ��
// ������ġ�� ������� send_cnt�� ���� �ߴ� ��ȣ�� ������
// ������ġ�� �����Ͱ� ������ 10�� �ð����� ȸ�� AMI�� ������ ����

public class send_cnt extends ViewableAtomic{
	
	String name;
	protected double processing_time;
	
	protected out_message o_m; // ���� �簳�ƴٰ� �˸�
	
	public send_cnt()
	{
		this("send_cnt", 10);
	}
	public send_cnt(String _name, double Processing_time)
	{
		super(_name);
		name = _name;
		
		addInport("in"); // ���� �簳�ƴ��� ��ȣ ���� ��Ʈ
		addOutport("out"); // 10���� ��ȣ ���� -> ȸ�� AMI�� ��������
		
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
			o_m = (out_message)x.getValOnPort("in", i); // output data ����
			// � �� �޾Ҵ��� 0 -> ���� �ߴ�, 1 -> ���� �簳, 2-> ������ġ�� ������ ����
			if(o_m.messageID == 1) // ���� �簳
			{
				System.out.println("SEND_CNT -> ȸ�� AMI�� ���� �簳");
				holdIn("START", processing_time); // ���� �簳 10
			}
			else if(o_m.messageID == 0) // ���� �ߴ�
			{
				System.out.println("SEND_CNT -> ȸ�� AMI�� ���� �ߴ�");
				holdIn("STOP", INFINITY); // ���� �ߴ�
			}
			else // ������ġ�� ������ �������� ����
			{
				System.out.println("SEND_CNT -> ������ġ�� ���� ������ ����");
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
