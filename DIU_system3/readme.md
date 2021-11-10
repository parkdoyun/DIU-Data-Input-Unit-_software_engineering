
Data Input Unit (DIU) system-2 의 요구 사항 2

1. 요구사항2는 요구사항1에서 추가하거나 수정합니다. 요구사항2와 관련없는 요구사항1은 정상 작동합니다.
2. 회사AMI는 DIU로부터 Input Data message를 입력받는다. 입력받은 message의 갯수가 3개가 되면, 회사AMI는 DIU로 전송관련message(전송중단을 의미함)를 보내고(message 갯수는 0으로 setting), 전력 Prediction을 수행합니다. 전력 Prediction time은 50 simulation time입니다. 전력 Prediction 수행이 끝나면, 회사AMI는 DIU로 전송관련message(전송재개를 의미함)를 보냅니다.
4. DIU는 회사AMI로부터 전송관련message를 입력받으면, 전송관련message를 식별하여, 회사AMI로의 전송을 중단 또는 재개합니다.
5. DIU는 message 저장할 저장장치가 없으므로, 전송중단 시간 동안에는 회사AMI로 보낼 message들을 저장하지 못하여 소실됩니다.
6. 요구사항 검증을 위하여 generator는 message output의 시간간격을 13 simulation time으로 하고, 연속 10개의 회사AMI로 향하는 message를 DUI로 전송합니다.

/////////////////////////////////////////////////////////////////////////////////////////

전송 Data Format
1. 회사AMI에서 DIU로 가는 전송관련message에서, content는 AMI-ID, DIU-ID, 전송중단/재개 모두 3개이다. 
(ex: 11, 1001, 0)은 전송중단을 의미합니다. (ex: 11, 1001, 1)은 전송재개을 의미합니다.

////////////////////////////////////////////////////////////////////////////////////////

Data Input Unit (DIU) system-3 의 요구 사항 3

1. 요구사항3은 요구사항1과 요구사항2에서 추가하거나 수정합니다. 요구사항3와 관련없는 요구사항1과 요구사항2는 정상 작동합니다.
2. DIU는 message 저장할 저장장치를 가지고 있습니다. 전송중단 시간 동안에는 회사AMI로 보낼 message들을 저장하여 소실되지 않습니다. 전송이 재개되면 저장된 message를 회사AMI로 시간간격을 10 simulation time 하여 전송합니다.


![DIU](https://user-images.githubusercontent.com/60337066/141144580-ba8d9d9b-c82c-4062-8b1e-3adc243c4f96.PNG)
▲ [DIU system2 State Transition Diagram]



![DIU 그림](https://user-images.githubusercontent.com/60337066/141145252-658ca5c1-67eb-457d-876a-54884ffe191b.PNG)
▲ [DIU system3 State Transition Diagram]



![회사 AMI](https://user-images.githubusercontent.com/60337066/141144994-b214de62-eff8-4747-8e9c-6733aebbde59.PNG)
▲ [AMI company State Transition Diagram]
