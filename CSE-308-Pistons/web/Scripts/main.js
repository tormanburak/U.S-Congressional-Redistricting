var precinctBtn = false;
var districtBtn = false;
var stateDataRetrieved = false;
var kyDataRetrieved = false;
var txDataRetrieved = false;
var laDataRetrieved = false;
var isDataBeingRetrieved = false;
var phase1Btn = false;

function openInfo(){
    document.getElementById("infoPanel").style.width = "500px";
    var mapSize = parseInt(document.getElementById("map").style.width.split("px")[0])
        - parseInt(document.getElementById("infoPanel").style.width.split("px")[0]);
    document.getElementById("map").style.width = String(mapSize) + "px";
    document.getElementById("displayBtn").style.right = document.getElementById("infoPanel").style.width;

    map.invalidateSize();
}

function closeInfo(){
    var mapSize = parseInt(document.getElementById("map").style.width.split("px")[0])
        + parseInt(document.getElementById("infoPanel").style.width.split("px")[0]);
    document.getElementById("infoPanel").style.width = "0";
    document.getElementById("map").style.width = String(mapSize) + "px";
    document.getElementById("displayBtn").style.right = "0px";

    map.invalidateSize();
}

function openNav(){
    document.getElementById("mySidebar").style.width = "325px";
    var mapSize = parseInt(document.getElementById("map").style.width.split("px")[0])
        - parseInt(document.getElementById("mySidebar").style.width.split("px")[0]);
    document.getElementById("map").style.left = document.getElementById("mySidebar").style.width;
    document.getElementById("map").style.width = String(mapSize) + "px";

    map.invalidateSize();
}

function closeNav(){
    var mapSize = parseInt(document.getElementById("map").style.width.split("px")[0])
        + parseInt(document.getElementById("mySidebar").style.width.split("px")[0]);
    document.getElementById("mySidebar").style.width = "0";
    document.getElementById("map").style.left = document.getElementById("mySidebar").style.width;
    document.getElementById("map").style.width = String(mapSize) + "px";

    map.invalidateSize();
}

function setUpForTheSelectedState(){
    if(table){
        clearTable();
    }
    if(phase1Btn){
        clickOnOriginal();
        document.getElementById("phase1Btn").style.display = "none";
    }
    precinctBtn = false;
    districtBtn = false;
    // buttons for display precinct or district
    if (document.getElementById("dropdown").value == "Default") {
        document.getElementById("display-precinct").style.display = "none";
        document.getElementById("display-district").style.display = "none";
    } else {
        document.getElementById("display-precinct").style.display = "block";
        document.getElementById("display-district").style.display = "block";
    }
    showStateData();
}

function intializeStateData(){
    var selectedState = document.getElementById("dropdown").value;
    if(isDataBeingRetrieved){
        return;
    }else if(kyDataRetrieved && selectedState == "KY" ){
        return;
    }else if(txDataRetrieved && selectedState == "TX"){
        return;
    }
    else if(laDataRetrieved && selectedState == "LA"){
        return;
    }
    var userInput = {
        selectedState: selectedState,
        operation: "DISPLAY_DISTRICTS",
    };
    isDataBeingRetrieved = true;
    document.getElementById("loader").style.display = "block";
    $.ajax({
        url: 'MainPage',
        type: "POST",
        data: {userInput: JSON.stringify(userInput)},
        success: [function (responseText) {
            debugger;
            var data = JSON.parse(responseText);

            var districtData = [];
            var district;
            for (var i = 0; i < data.districts.length; i++) {
                data.districts[i].coordinates = data.districts[i].coordinates.replace(/'/g, '"');
                district = {
                    type: "Feature",
                    districtID:  data.districts[i].districtID,
                    neighborDistrict: data.districts[i].neighborDistrct,
                    geometry: JSON.parse(data.districts[i].coordinates),
                    demProperties: data.districts[i].demographic,
                    electionProperties: data.districts[i].electionData
                }
                districtData.push(district);
            }
            var precinctData = [];

            for (var i = 0; i < data.precincts.length; i++) {
                var coordinate = JSON.parse(data.precincts[i].coordinates.replace(/'/g, '"'));
                var precinct = {
                    type: "Feature",
                    precinctID:  data.precincts[i].precinctID,
                    geometry: coordinate[0],
                    demProperties: data.precincts[i].demographic,
                    electionProperties: data.precincts[i].electionData
                }
                precinctData.push(precinct);
            }
            precinctsData.features = precinctData;
            districtsData.features = districtData;
            setDistrictColor();
            setPrecinctColor();
            changeMapDataDependingOnZoomLevel();
            statesData2 = data.state;
            stateDataRetrieved = true;
            isDataBeingRetrieved =false;
            google.charts.setOnLoadCallback(displayStateDemo);
            google.charts.setOnLoadCallback(drawStateElectionChart);
            document.getElementById("loader").style.display = "none";
            console.log(districtsData);
            console.log(precinctsData);

        }],
        error : function(jqXHR, textStatus, errorThrown){
            alert(textStatus.status);
            document.getElementById("loader").style.display = "none";
            stateDataRetrieved =false;
            isDataBeingRetrieved =false;
        }
    });
}

function showBothStateCharts(){
    google.charts.setOnLoadCallback(displayStateDemo);
    google.charts.setOnLoadCallback(drawStateElectionChart);
}

function showStateData(){
    intializeStateData();
    if(stateDataRetrieved){
        showBothStateCharts();
    }
    changeMapData(statesData);
    zoom();
}

function checkVotingDropDown(){
    if (document.getElementById('votingType').value == 'Default') {
        document.getElementById('2018VotingYear').disabled = false;
        return;
    }
    if (document.getElementById('votingYearDropDown').value == 'Default') {
        document.getElementById('Presidential').disabled = false;
        return;
    }
    if (document.getElementById('votingYearDropDown').value == '2018') {
        document.getElementById('Presidential').disabled = true;
        return;
    } else if (document.getElementById('votingYearDropDown').value == '2016') {
        document.getElementById('Presidential').disabled = false;
        return;
    }
    if (document.getElementById('votingType').value == 'Presidential') {
        document.getElementById('2018VotingYear').disabled = true;
        return;
    } else if (document.getElementById('votingType').value == 'Congressional') {
        document.getElementById('2018VotingYear').disabled = false;
        return;
    }
}

function clickOnDisplayPrecincts(){
    document.getElementById("loader").style.display = "block";
    precinctBtn = !precinctBtn;
    districtBtn = false;
    if (precinctBtn) {
        document.getElementById("display-precinct").style.backgroundColor = "red";
        document.getElementById("display-district").style.backgroundColor = "black";
        if(document.getElementById("dropdown").value != "Default"){
            changeMapData(precinctsData);
        }
    } else {
        document.getElementById("display-precinct").style.backgroundColor = "black";
        changeMapDataDependingOnZoomLevel();
    }
    document.getElementById("loader").style.display = "none";
}

function clickOnDisplayDistricts(){
    document.getElementById("loader").style.display = "block";
    districtBtn = !districtBtn;
    precinctBtn = false;
    if (districtBtn) {
        document.getElementById("display-district").style.backgroundColor = "red";
        document.getElementById("display-precinct").style.backgroundColor = "black";
        if(document.getElementById("dropdown").value != "Default") {
            changeMapData(districtsData);
        }
    } else {
        document.getElementById("display-district").style.backgroundColor = "black";
        changeMapDataDependingOnZoomLevel();
    }

    document.getElementById("loader").style.display = "none";
}

function setDistrictColor(){
    districtsData.features.sort(function (a, b) { return a.districtID - b.districtID; });
    districtsData.features.sort(function (a, b) { return b.neighborDistrict.length - a.neighborDistrict.length; });

    for(var i = 0; i < districtsData.features.length; i++){
        districtsData.features[i]["color"] = "none";
    }

    for(var i = 0; i < districtsData.features.length; i++){
        var color = {0: true, 1: true, 2: true, 3: true};
        var dist = districtsData.features[i];
        for(var j = 0; j < dist.neighborDistrict.length; j++){
            for(var k = 0; k < districtsData.features.length; k++){
                var neighbor = districtsData.features[k];
                if(neighbor.districtID == dist.neighborDistrict[j]){
                    if(neighbor.color != "none"){
                        color[neighbor.color] = false;
                        continue;
                    }
                }
            }
        }

        for(var key in color){
            if(color[key]){
                dist.color = key;
                break;
            }
        }

        if(dist.color == "none"){
            dist.color = 4;
        }
    }
}

function setPrecinctColor(){
    for(var i = 0; i < precinctsData.features.length; i++){
        precinctsData.features[i].demProperties.totalPopulation > 2000 ? precinctsData.features[i]["color"] = 7 :
            precinctsData.features[i].demProperties.totalPopulation > 1750  ? precinctsData.features[i]["color"] = 6 :
                precinctsData.features[i].demProperties.totalPopulation > 1500  ? precinctsData.features[i]["color"] = 5 :
                    precinctsData.features[i].demProperties.totalPopulation > 1250  ? precinctsData.features[i]["color"] = 4 :
                        precinctsData.features[i].demProperties.totalPopulation > 700   ? precinctsData.features[i]["color"] = 3 :
                            precinctsData.features[i].demProperties.totalPopulation > 300   ? precinctsData.features[i]["color"] = 2 :
                                precinctsData.features[i].demProperties.totalPopulation > 100   ? precinctsData.features[i]["color"] = 1 :
                                    precinctsData.features[i]["color"] = 0
    }
}

function clickOnOriginal(){
    document.getElementById("colorTheme-button").style.display = "block";
    if(phase1TableStatus){
        clearTable2();
    }
    document.getElementById("display-district").style.display = "block";
    document.getElementById("display-precinct").style.display = "block";
    document.getElementById("new").style.backgroundColor = "black";
    document.getElementById("original").style.backgroundColor = "red";
    phase1Btn = false;

    changeMapDataDependingOnZoomLevel();
}

function clickOnNew(){
    document.getElementById("colorTheme-button").style.display = "none";
    //clearCharts();
    if(phase1Btn){
        statePieChart.clearChart();
        stateBarChart.clearChart();
    }
    document.getElementById("district-precinct-chart-title").style.display = "none";
    pieChart.clearChart();
    barChart.clearChart();
    document.getElementById("display-district").style.display = "none";
    document.getElementById("display-precinct").style.display = "none";
    document.getElementById("new").style.backgroundColor = "red";
    document.getElementById("original").style.backgroundColor = "black";
    phase1Btn = true;

    changeMapData(phase1PrecinctsData);

    //showBothStateCharts();
    //changeMapData(mergedDistrctData);
}

function changeOnNumOfDistricts() {
    var value = parseInt(document.getElementsByClassName("ui-slider")[1].getElementsByTagName("a")[0].getAttribute("aria-valuenow"))/parseInt(document.getElementById("phase1-districts").value) * 100;
    document.getElementsByClassName("ui-slider")[1].getElementsByTagName("input")[0].setAttribute("max", document.getElementById("phase1-districts").value);
    if(value < 100)
        document.getElementsByClassName("ui-slider")[1].getElementsByTagName("a")[0].setAttribute("style","left: " + String(value) + "%;");
    else{
        document.getElementsByClassName("ui-slider")[1].getElementsByTagName("input")[0].value = document.getElementById("phase1-districts").value;
        document.getElementsByClassName("ui-slider")[1].getElementsByTagName("a")[0].setAttribute("aria-valuenow", document.getElementById("phase1-districts").value);
        document.getElementsByClassName("ui-slider")[1].getElementsByTagName("a")[0].setAttribute("aria-valuetext", document.getElementById("phase1-districts").value);
        document.getElementsByClassName("ui-slider")[1].getElementsByTagName("a")[0].setAttribute("title", document.getElementById("phase1-districts").value);
    }
}