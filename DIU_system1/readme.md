
<h2>Data Input Unit (DIU) 의 목적</h2><br>
Data Input Unit (DIU) 는 Input Data를 식별하고, 분류하고, 부합되는 AMI로 Output Data를 전송한다.<br><br>
<h2>Data Input Unit (DIU) 의 요구 사항 1</h2><br>
1. Data Input Unit (DIU) 는 전력을 사용하는 많은 전력객체 (전력객체종류에는 주택, 학교, 회사가 있음)들로부터 Input Data를 입력받는다.<br>
2. Data Input Unit (DIU) 는 Input Data를 식별하고, 분류하고, 저장하고, 종류AMI 와 지역AMI 중에 부합되는 AMI로 Output Data를 전송한다.<br>
3. 종류AMI는 주택, 학교, 회사로 구분하고, 지역AMI는 용현1동, 용현2동, 용현3동으로 구분한다.<br>
4. 종류AMI 는 주택AMI, 학교AMI, 회사AMI 이고, 지역AMI 는 용현1동AMI, 용현2동AMI, 용현3동AMI 이다.<br>
5. Input Data의 식별은 Input Data의 message 에 8개의 content 가 모두 존재하고, DIU-ID가 맞으면, Data 분류를 진행하고, content 가 모두 존재하지 않거나, DIU-ID가 맞지 않으면, 해당 전력객체ID 에 Input Data를 재요청하는 Output을 내보낸다.<br>
6. Input Data의 분류는 종류AMI(주택AMI, 학교AMI, 회사AMI) , 지역AMI(용현1동AMI, 용현2동AMI, 용현3동AMI), 모두 6개의 AMI에 부합되는 Input Data를 분류한다.<br>
7. 분류된 Data 는 부합되는 각각의 AMI 에 맞도록 Output Data를 만들어서 부합되는 각각의 AMI 에 전달되도록 한다.<br><br>
//////////////////////////////////////////////////////////////<br>
<h3>전송 Data Format</h3><br>
1. Input Data의 format 은 message 이고, content 는 전력객체ID, meassge-ID, DIU-ID, 종류, 주소, 지역, 날짜, 1일 사용전력량, 모두 8개이다. (ex, 10001, 1000001, 1001, 회사, 용현1동, 인천시 ~~ , 2021년 8월 1일, 1000kw). DIU-ID 는 Data Input Unit-ID 이다.
2. 해당 전력객체ID 에 Input Data를 재요청하는 Output 의 format 은 meassge 이고, content 는 전력객체 ID, meassge-ID, DIU-ID, 재요청, 모두 4개이다. (ex, 10001, 1000001, 1001, 재요청)
3. Output Data의 format 은 message 이고, content 는 DIU-ID, meassge-ID, AMI-ID, 종류, 주소, 지역, 날짜, 1일 사용전력량, 모두 8개이다. (ex, 1001, 1000001, 11, 회사, 용현1동, 인천시 ~~ , 2021년 8월 1일, 1000kw). DIU-ID 는 Data Input Unit-ID 이다.<br><br>
//////////////////////////////////////////////////////////////
<br><br><br>
![DIU 그림](https://user-images.githubusercontent.com/60337066/141147999-7249590f-c165-426a-8805-8f6368a047ae.PNG)
<br>▲ [DIU State Transition Diagram]
