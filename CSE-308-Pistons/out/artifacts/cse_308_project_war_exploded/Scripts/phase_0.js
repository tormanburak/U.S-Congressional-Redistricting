var selectedRow;
function startPhase0(){
    if(tableStatus){
        clearTable();
    }
    statePieChart.clearChart();
    stateBarChart.clearChart();
    pieChart.clearChart();
    barChart.clearChart();

    //displayTable();

    if(!isThresholdsSet){
        return;
    }

    var userInput = {
        "thresholds": {
            "POPULATION_PERCENT_MIN":  document.getElementById("pop-min").value,
            "POPULATION_PERCENT_MAX": document.getElementById("pop-max").value,
            "VOTING_PERCENT_MIN": document.getElementById("voting-min").value,
            "VOTING_PERCENT_MAX": document.getElementById("voting-max").value
        },
        votingYear: document.getElementById("votingYearDropDown").value,
        votingType: document.getElementById("votingType").value,
        // popMin: document.getElementById("pop-min").value,
        // popMax: document.getElementById("pop-max").value,
        // votingMin: document.getElementById("voting-min").value,
        // votingMax: document.getElementById("voting-max").value,
        operation: "PHASE_0",
    };

    document.getElementById("loader").style.display = "block";
    $.ajax({
        url: 'MainPage',
        type: "POST",
        data: {userInput: JSON.stringify(userInput)},
        success: [function (responseText) {
            var json = JSON.parse(responseText);
            console.log(json);
            var dataSet = jsonToDataForTable(json);
            console.log(dataSet);
            displayTable(dataSet);
            var summaryDataSet = constructSummaryData(dataSet);
            console.log(summaryDataSet);
            displaySummaryTable(summaryDataSet);

            document.getElementById("summary-table").style.display = "table";
            document.getElementById("table").style.display = "table";

            document.getElementsByClassName("table")[0].getElementsByTagName("h1")[0].style.display = "block";
            document.getElementsByClassName("table")[0].getElementsByTagName("h1")[1].style.display = "block";
            document.getElementById("loader").style.display = "none";
        }],
        error : function(jqXHR, textStatus, errorThrown){
            document.getElementById("loader").style.display = "none";
            alert(textStatus.status+ " "+ jqXHR + " " + errorThrown);
        }
    });
}

function isThresholdsSet(){
    var pop_Min = document.getElementById("pop-min").value
    var pop_Max = document.getElementById("pop-max").value
    var voting_Min = document.getElementById("voting-min").value
    var voting_Max = document.getElementById("voting-max").value
    var votingYear = document.getElementById("votingYearDropDown").value
    var votingType = document.getElementById("votingType").value

    if(pop_Min == null){
        return false;
    }else if(pop_Max == null){
        return false;
    }else if(voting_Min == null){
        return false;
    }else if(voting_Max == null){
        return false;
    }else if(votingYear.localeCompare("Default")){
        return false;
    }else if(votingType.localeCompare("Default")){
        return false;
    }else{
        return true;
    }
}

function highlightVotingBloc(data){
    selectedRow = data;
    geojson.clearLayers();
    geojson = L.geoJson(precinctsData, {
        style: votingBlocStyle,
        onEachFeature: onEachFeatureForPrecinctOrDistrict
    }).addTo(map);
}

function votingBlocStyle(feature) {
    return {
        fillColor: getVotingBlocColor(feature),
        weight: 1,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.6
    };
}

function getVotingBlocColor(feature){
    var idArray = precinctIds[selectedRow[0] + "_" + selectedRow[2]];
    for(var i = 0; i < idArray.length; i++){
        if(feature.precinctID == idArray[i]){
            return document.getElementById("colorTheme").value;
        }
    }
    return "white";

    /*
    var idArray = precinctIds[selectedRow[0]];
    for(var i = 0; i < idArray.length; i++){
        if(feature.precinctID == idArray[i]){
            return document.getElementById("colorTheme").value;
        }
    }
    return "white";
     */
}