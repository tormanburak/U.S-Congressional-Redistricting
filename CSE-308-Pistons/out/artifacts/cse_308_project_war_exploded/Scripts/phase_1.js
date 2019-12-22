var phase1Data = {"type": "FeatureCollection", "features": []};
var mergedDistrctData = {"type": "FeatureCollection", "features": []};

var myMap2;
var myColorMap;
var myNeighborMap;
var stateTableData;
var districtTableData;

function startPhase1() {
    if(tableStatus){
        clearTable();
    }

    if(phase1TableStatus){
        clearTable2();
    }
    document.getElementById("phase2").style.display = "hidden";
    debugger;
    if (!isPhase1ThresholdsSet()) {
        return;
    }
    var isRunContinuous = $('#checkbox').is(':checked');
    if(isRunContinuous){
        userPreference = "FINAL"
    }else{
        userPreference= "ITERATE";
    }
    var numOfDistricts = document.getElementById("phase1-districts").value;
    var userInput = {
        minorityRace:  $('#race-dropdown').val(),
        minorityMin: document.getElementById("phase1-pop-min").value,
        minorityMax: document.getElementById("phase1-pop-max").value,
        runPreference: userPreference,
        desiredDistricts: numOfDistricts,
        desiredMajMinDistricts: document.getElementById("majmin-districts").value,
        operation: "PHASE_1"
    };
    $.ajax({
        url: 'MainPage',
        type: "POST",
        data: {userInput: JSON.stringify(userInput)},
        success: [function (responseText) {
            document.getElementById("loader").style.display = "none";
            if(!isRunContinuous){
                document.getElementById("cont").style.display= "block";
            }
            var data = JSON.parse(responseText);
            console.log(data);
            var phase1Obj = [];
            var obj;
            for (var i = 0; i < data.length; i++) {
                obj = {
                    type: "Feature",
                    precinctID: data[i].precinctID,
                    precinctsInCluster: data[i].precinctsInCluster,
                    demProperties: data[i].demographic,
                    electionProperties: data[i].electionData,
                    neighbors: data[i].neighbors,
                    isMajMin: data[i].isMajMin
                }
                phase1Obj.push(obj);
            }
            phase1Data.features = phase1Obj;
            debugger;
            constructPhase1Data();
            document.getElementById("phase1Btn").style.display = "block";
            clickOnNew();
            showBothStateCharts();
            stateTableData = constructStatePopTable(statesData2);
            districtTableData = constructDistrictPopTable(phase1Data);
            displayStatePopTable(stateTableData);
            displayDistrictPopTable(districtTableData);
            if(data.length == numOfDistricts || data.length == numOfDistricts-1 || data.length == numOfDistricts+2){
                // remove continue button
                document.getElementById("cont").style.display= "none";
            }
        }],
        error: function (jqXHR, textStatus, errorThrown) {
            document.getElementById("loader").style.display = "none";
            alert(textStatus.status + " " + jqXHR + " " + errorThrown);
        }
    });
}
function isPhase1ThresholdsSet() {
    //add race dropdwon check
    var pop_Min = document.getElementById("phase1-pop-min").value
    var pop_Max = document.getElementById("phase1-pop-max").value

    if (pop_Min == null) {
        return false;
    } else return pop_Max != null;
}

function constructPhase1Data(){
    phase1PrecinctsData = precinctsData;
    myMap2 = {};
    myNeighborMap = [];
    myColorMap = {};


    for(var i = 0; i < phase1Data.features.length; i++){
        if(phase1Data.features[i].isMajMin)
            myColorMap[String(phase1Data.features[i].precinctID)] = 61;
        else
            myColorMap[String(phase1Data.features[i].precinctID)] = i % 60;
    }

    for(var i = 0; i < phase1Data.features.length; i++){
        myMap2[String(phase1Data.features[i].precinctID)] = phase1Data.features[i].precinctID;
        for(var j = 0; j < phase1Data.features[i].precinctsInCluster.length; j++){
            myMap2[String(phase1Data.features[i].precinctsInCluster[j])] = phase1Data.features[i].precinctID;
        }
//         for(var j = 0; j < phase1Data.features[i].neighbors.length; j++){
//             if(i == 0)
//                 myNeighborMap.push(phase1Data.features[i].precinctID);
// //                myNeighborMap[String(phase1Data.features[i].neighbors[j])] = phase1Data.features[i].precinctID;
//         }
    }

    for(var i = 0; i < phase1PrecinctsData.features.length; i++){
        for(var j = 0; j < phase1Data.features.length; j++){
            if(myMap2[String(phase1PrecinctsData.features[i].precinctID)] == phase1Data.features[j].precinctID){
                phase1PrecinctsData.features[i]["phase1"] = {};
                phase1PrecinctsData.features[i]["phase1"]["demProperties"] = phase1Data.features[j].demProperties;
                phase1PrecinctsData.features[i]["phase1"]["electionProperties"] = phase1Data.features[j].electionProperties;
                continue;
            }
        }
    }
}

function highlightCluster(data){
    selectedRow = data;
    geojson.clearLayers();
    geojson = L.geoJson(precinctsData, {
        style: clusterStyle,
        onEachFeature: onEachFeatureForPrecinctOrDistrict
    }).addTo(map);
}

function clusterStyle(feature){
    if(myMap2[String(feature.precinctID)] == selectedRow[0]){
        return {
            fillColor: clusterColorSet[myColorMap[String(myMap2[String(feature.precinctID)])]],
            weight: 2,
            opacity: 0.7,
            color: clusterColorSet[myColorMap[String(myMap2[String(feature.precinctID)])]],
            dashArray: '',
            fillOpacity: 1
        };
    }
    return {
        fillColor: "white",
        weight: 0,
        opacity: 0.7,
        color: "white",
        dashArray: '',
        fillOpacity: 1
    };
}

/*
function displayPhase1(){
    mergedDistrctData = {"type": "FeatureCollection", "features": []};
    var myMap = {};

    for(var i =0; i < precinctsData.features.length; i++){
        myMap[precinctsData.features[i].precinctID] = precinctsData.features[i];
    }
    debugger;
    var counter = 0;
    var mergeDistrict ;
    for(var i =0; i < phase1Data.features.length; i++){
        var demProperties = constructDemProperties(i);
        var electionProperties = constructElectionProperties(i);

        var polyarr = [];
        var poly1 = (myMap[phase1Data.features[i].precinctID].geometry.type == 'Polygon')
            ? turf.polygon(myMap[phase1Data.features[i].precinctID].geometry.coordinates)
            : turf.multiPolygon(myMap[phase1Data.features[i].precinctID].geometry.coordinates);
        polyarr.push(poly1);
        for(var j =0 ; j < phase1Data.features[i].precinctsInCluster.length; j++){
            var poly2 = (myMap[phase1Data.features[i].precinctsInCluster[j]].geometry.type == 'Polygon')
                ? turf.polygon(myMap[phase1Data.features[i].precinctsInCluster[j]].geometry.coordinates)
                : turf.multiPolygon(myMap[phase1Data.features[i].precinctsInCluster[j]].geometry.coordinates);
            polyarr.push(poly2);
        }
        poly1 = turf.union.apply(this,polyarr);
        mergeDistrict = {
            type:"Feature",
            precinctID: phase1Data.features[i].precinctID,
            geometry: poly1["geometry"],
            properties: poly1["properties"],
            demProperties: demProperties,
            electionProperties: electionProperties,
            type: poly1["type"]
        }
        console.log(mergeDistrict);
        debugger;
        //displayonmap
        mergedDistrctData.features.push(mergeDistrict);
    }
    geojson.clearLayers();
    geojson = L.geoJson(mergedDistrctData, {
        style: districtStyle,
        onEachFeature: onEachFeatureForPrecinctOrDistrict
    }).addTo(map);
  //   //turf.union(turf.polygon(myMap[1].coordinates),turf.polygon(myMap[2].coordinates));
  //   var multiPolgon = turf.multiPolygon(myMap[10]);
  // //  var poly1 = turf.polygon(multiPolgon);
  //   var multiPolgon2 = turf.multiPolygon(myMap[1]);
  //  // var poly2 = turf.polygon(multiPolgon2);
  //   var union = turf.union(multiPolgon, multiPolgon2);

}


function constructDemProperties(i){
    var demProperties = {};

    var totalPop = phase1Data.features[i].demProperties.totalPopulation;
    var highestRacePop = phase1Data.features[i].demProperties.highestRacePop;
    var demID = phase1Data.features[i].demProperties.demID;
    var pop = {};
    pop["AFRICAN_AMERICANS"] = phase1Data.features[i].demProperties.populations.AFRICAN_AMERICANS;
    pop["AMERICAN_INDIAN"] = phase1Data.features[i].demProperties.populations.AMERICAN_INDIAN;
    pop["ASIAN"] = phase1Data.features[i].demProperties.populations.ASIAN;
    pop["NATIVE_AMERICAN"] = phase1Data.features[i].demProperties.populations.NATIVE_AMERICAN;
    pop["OTHERS"] = phase1Data.features[i].demProperties.populations.OTHERS;
    pop["WHITE"] = phase1Data.features[i].demProperties.populations.WHITE;

    demProperties["totalPopulation"] = totalPop;
    demProperties["populations"] = pop;
    demProperties["highestRacePop"] = highestRacePop;
    demProperties["demID"] = demID;

    return demProperties;
}

function constructElectionProperties(i){
    var electionProperties = {};
    var cong2016dem = phase1Data.features[i].electionProperties.congressionalDem2016;
    var cong2018dem = phase1Data.features[i].electionProperties.congressionalDem2018;
    var cong2016other = phase1Data.features[i].electionProperties.congressionalOthers2016;
    var cong2018other = phase1Data.features[i].electionProperties.congressionalOthers2018;
    var cong2016rep = phase1Data.features[i].electionProperties.congressionalRep2016;
    var cong2018rep = phase1Data.features[i].electionProperties.congressionalRep2018;
    var pres2016dem = phase1Data.features[i].electionProperties.presDem2016;
    var pres2016other = phase1Data.features[i].electionProperties.presOthers2016;
    var pres2016rep = phase1Data.features[i].electionProperties.presRep2016;
    var electionDataID = phase1Data.features[i].electionProperties.electionDataID;

    electionProperties["congressionalDem2016"] = cong2016dem;
    electionProperties["congressionalDem2018"] = cong2018dem;
    electionProperties["congressionalOthers2016"] = cong2016other;
    electionProperties["congressionalOthers2018"] = cong2018other;
    electionProperties["congressionalRep2016"] = cong2016rep;
    electionProperties["congressionalRep2018"] = cong2018rep;
    electionProperties["electionDataID"] = electionDataID;
    electionProperties["presRep2016"] = pres2016rep;
    electionProperties["presOthers2016"] = pres2016other;
    electionProperties["presDem2016"] = pres2016dem;

    return electionProperties;

}
*/

//function merge()
