var oldDistricts = [];
var newDistricts = [];
var phase2moves = [];
var phase2Btn = false;
var index=0;
var delay = 500; // 2000ms = 2 second
var algorithmRun = false; // run annealing
var infoSidebar = false;

function startPhase2() {
    document.getElementById("phase1Btn").style.display = "none";
    var userInput = {
        operation: "PHASE_2"
    };
    clearCharts();
    if(tableStatus){
        clearTable();
    }
    debugger;
    if(phase1TableStatus){
        clearTable2();
    }
    if(phase2TableStatus){
        clearTable3();
    }
    $.ajax({
        url: 'MainPage',
        type: "POST",
        data: {userInput: JSON.stringify(userInput)},
        success: [function (responseText) {
            debugger;
            var data = JSON.parse(responseText);
            phase1Btn = false;
            phase1TableStatus = false;
            phase2Btn = true;
            constructPhase2Data(data);
            var tableData = constructPhase2TableData();
            displayPhase2Table(tableData);
            if(document.getElementById("infoPanel").style.width != "500px"){
                openInfo();
            }
            if(!algorithmRun){
                algorithmRun = true;
                index = 0;
                increment();
            }

        }],
        error: function (jqXHR, textStatus, errorThrown) {
            document.getElementById("loader").style.display = "none";
            alert(textStatus.status + " " + jqXHR + " " + errorThrown);
        }
    });
}

function constructPhase2Data(data){
    for(var x in data.oldDistricts){
        var district = {
            id : x,
            gerryManderScore : data.oldDistricts[x]
        }
        oldDistricts.push(district);
    }
    for(var x in data.newDistricts){
        var district = {
            id : x,
            gerryManderScore : data.newDistricts[x]
        }
        newDistricts.push(district);
    }

    for(var i = 0 ; i < data.set.length;i++){
        var object = {
            pickedPrecinctID: data.set[i].pickedPrecinctID,
            sourceDistrictID: data.set[i].sourceDistrictID,
            targetDistrictID: data.set[i].targetDistrictID,
            targetOldObjFunc: data.set[i].targetOldObjFunc,
            targetNewObjFunc: data.set[i].targetNewObjFunc,
            makeMove: data.set[i].makeMove
        };
        phase2moves.push(object);
    }
}
var countNeg = 0;
function increment() {
    if(algorithmRun) {
        setTimeout(function () {
            index++;
            if (index <= phase2moves.length) {
                document.getElementById("precinctId").innerHTML = phase2moves[index].pickedPrecinctID;
                document.getElementById("districtId").innerHTML = phase2moves[index].targetDistrictID;
                document.getElementById("oldObjFunc").innerHTML = phase2moves[index].targetOldObjFunc;
                document.getElementById("newObjFunc").innerHTML = phase2moves[index].targetNewObjFunc;
                document.getElementById("algState").innerHTML = "Running";
                changeMapData(precinctsData);
                //count consecutive non-positive moves, if its back to back 10, break out.
                if (phase2moves[index].makeMove == true) {
                    countNeg = 0;
                    document.getElementById("decision").innerHTML = "Accepted";
                } else {
                    countNeg++;
                    document.getElementById("decision").innerHTML = "Rejected";
                    if (countNeg == 10) {
                        stopPhase2();
                    }
                }
                increment();
            }
        }, delay);
    }else{
        document.getElementById("algState").innerHTML = "Terminated";
    }
}
function stopPhase2(){
    algorithmRun = false;
}
