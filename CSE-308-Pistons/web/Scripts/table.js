var precinctIds = {};
var table, summaryTable;
var stateTable, districtTable;
var phase2Table;
var tableStatus = false;
var phase1TableStatus = false;
var phase2TableStatus = false;

function jsonToDataForTable(json){
    console.log(json);
    var data = [];
    for(var i = 0; i < json.length; i++){
        data.push([json[i]["precinctId"], json[i]["eligibleRace"], String(json[i]["eligibleRacePercent"]) +"%", json[i]["winningParty"], String(json[i]["winningPartyPercent"])+"%"]);
    }
    console.log(data);
    return data;
}

function constructSummaryData(dataSet){
    var dictionary = {};
    for(var i = 0; i < dataSet.length; i ++) {
        var id = dataSet[i][0];
        var race = dataSet[i][1];
        var race_percentage = parseFloat(dataSet[i][2].split("%")[0]);
        var party = dataSet[i][3];
        var party_percentage = parseFloat(dataSet[i][4].split("%")[0]);

        var key = race + party;

        if (!(key in dictionary)) {
            dictionary[key] = [race, race_percentage, party, party_percentage, [id]];
        } else {
            dictionary[key][4].push(id);
            var count = dictionary[key][4].length;
            dictionary[key][3] = ((dictionary[key][3] * (count - 1)) + party_percentage) / count;
            dictionary[key][1] = ((dictionary[key][1] * (count - 1)) + race_percentage) / count;
        }
    }

    var race_dictionary = {};
    for (var key in dictionary){
        var race = dictionary[key][0];
        if(!(race in race_dictionary)){
            race_dictionary[race] = [[dictionary[key][0], dictionary[key][1], dictionary[key][2], dictionary[key][3], dictionary[key][4].length, dictionary[key][4]]];
        }else{
            race_dictionary[race].push([dictionary[key][0], dictionary[key][1], dictionary[key][2], dictionary[key][3], dictionary[key][4].length, dictionary[key][4]]);
        }
    }

    var summary_data = [];

    for(var key in race_dictionary){
        for(var i = 0; i < race_dictionary[key].length; i++){
            var idKey = key + "_" + race_dictionary[key][i][2]
            precinctIds[idKey] = race_dictionary[key][i][5];
            summary_data.push([race_dictionary[key][i][0], String(Math.floor(race_dictionary[key][i][1] * 100)/100) + "%", race_dictionary[key][i][2], String(Math.floor(race_dictionary[key][i][3] * 100)/100) + "%", race_dictionary[key][i][4]]);
        }
    }

    /*
    for(var key in race_dictionary){
        var highestParty = 0;
        var index = 0;
        for(var i = 0; i < race_dictionary[key].length; i++){
            if(highestParty < race_dictionary[key][i][3]){
                highestParty = race_dictionary[key][i][3];
                index = i;
            }
        }
        precinctIds[key] = race_dictionary[key][index][5];
        summary_data.push([race_dictionary[key][index][0], String(Math.floor(race_dictionary[key][index][1] * 100)/100) + "%", race_dictionary[key][index][2], String(Math.floor(race_dictionary[key][index][3] * 100)/100) + "%", race_dictionary[key][index][4]]);
    }
     */

    return summary_data;
}

function displayTable(dataSet){
    table = $('#table').DataTable({
        data: dataSet,
        columns: [
            {title: "ID"},
            {title: "Race"},
            {title: "Race<br>Percentage"},
            {title: "Party"},
            {title: "Party<br>Percentage"}
        ],
    });
    tableStatus = true;
    table.draw();
}

function displaySummaryTable(summaryDataSet){
    summaryTable = $('#summary-table').DataTable({
        data: summaryDataSet,
        columns: [
            {title: "Race"},
            {title: "Avg<br>Percentage"},
            {title: "Party"},
            {title: "Avg<br>Percentage"},
            {title: "Count"}
        ],
        paging: false,
        searching: false,
    });
    summaryTable.draw();
}

function clearTable(){
    tableStatus = false;
    table.destroy();
    summaryTable.destroy();
    document.getElementById("summary-table").style.display = "none";
    document.getElementById("table").style.display = "none";
    document.getElementsByClassName("table")[0].getElementsByTagName("h1")[0].style.display = "none";
    document.getElementsByClassName("table")[0].getElementsByTagName("h1")[1].style.display = "none";
}

function constructStatePopTable(stateData){
    var races = $('#race-dropdown').val();
    var data = [];
    data.push([stateData.stateName, "WHITE", stateData.demographic.whitePop, String(Math.floor(((stateData.demographic.whitePop / stateData.demographic.totalPopulation)*10000))/100) + "%"]);
    for(var j = 0; j < races.length; j++){
        if(races[j] == "AFRICAN_AMERICANS")
            if("africanAmericanPop" in stateData.demographic)
                data.push([stateData.stateName, races[j], stateData.demographic.africanAmericanPop, String(Math.floor(((stateData.demographic.africanAmericanPop / stateData.demographic.totalPopulation)*10000))/100) + "%"]);
            else
                data.push([stateData.stateName, races[j], 0, 0]);
        else if(races[j] == "HISPANIC")
            if("hispanicPop" in stateData.demographic)
                data.push([stateData.stateName, races[j], stateData.demographic.hispanicPop, String(Math.floor(((stateData.demographic.hispanicPop / stateData.demographic.totalPopulation)*10000))/100) + "%"]);
            else
                data.push([stateData.stateName, races[j], 0, 0]);
        else if(races[j] == "ASIAN")
            if("asianPop" in stateData.demographic)
                data.push([stateData.stateName, races[j], stateData.demographic.asianPop, String(Math.floor(((stateData.demographic.asianPop / stateData.demographic.totalPopulation)*10000))/100) + "%"]);
            else
                data.push([stateData.stateName, races[j], 0, 0]);
        else if(races[j] == "AMERICAN_INDIAN")
            if("americanIndianPop" in stateData.demographic)
                data.push([stateData.stateName, races[j], stateData.demographic.americanIndianPop, String(Math.floor(((stateData.demographic.americanIndianPop / stateData.demographic.totalPopulation)*10000))/100) + "%"]);
            else
                data.push([stateData.stateName, races[j], 0, 0]);
        else if(races[j] == "NATIVE_AMERICAN")
            if("nativeAmericanPop" in stateData.demographic)
                data.push([stateData.stateName, races[j], stateData.demographic.nativeAmericanPop, String(Math.floor(((stateData.demographic.nativeAmericanPop / stateData.demographic.totalPopulation)*10000))/100) + "%"]);
            else
                data.push([stateData.stateName, races[j], 0, 0]);
        else if(races[j] == "OTHERS")
            if("othersPop" in stateData.demographic)
                data.push([stateData.stateName, races[j], stateData.demographic.othersPop, String(Math.floor(((stateData.demographic.othersPop / stateData.demographic.totalPopulation)*10000))/100) + "%"]);
            else
                data.push([stateData.stateName, races[j], 0, 0]);
    }
    return data;
}

function displayStatePopTable(dataSet){
    stateTable = $('#state-pop-race-table').DataTable({
        data: dataSet,
        columns: [
            {title: "ID"},
            {title: "Race"},
            {title: "Total<br>Population"},
            {title: "Percentage"}
        ],
    });

    $( document ).ready(function() {
        if(phase1TableStatus){
            var dimension_col = null;
            var columnCount = $("#state-pop-race-table tr:first th").length;
            for (dimension_col = 0; dimension_col < columnCount; dimension_col++) {
                // first_instance holds the first instance of identical td
                var first_instance = null;
                var rowspan = 1;
                // iterate through rows
                $("#state-pop-race-table").find('tr').each(function () {

                    // find the td of the correct column (determined by the dimension_col set above)
                    var dimension_td = $(this).find('td:nth-child(' + dimension_col + ')');

                    if (first_instance == null) {
                        // must be the first row
                        first_instance = dimension_td;
                    } else if (dimension_td.text() == first_instance.text()) {
                        // the current td is identical to the previous
                        // remove the current td
                        dimension_td.remove();
                        ++rowspan;
                        // increment the rowspan attribute of the first instance
                        first_instance.attr('rowspan', rowspan);
                    } else {
                        // this cell is different from the last
                        first_instance = dimension_td;
                        rowspan = 1;
                    }
                });
            }
        }
    });

    document.getElementsByClassName("table")[0].getElementsByTagName("h1")[2].style.display = "block";
    document.getElementById("state-pop-race-table").style.display = "table";
    phase1TableStatus = true;
    stateTable.draw();
}

function constructDistrictPopTable(districtData){
    var races = $('#race-dropdown').val();
    var data = [];
    for(var i = 0; i < districtData.features.length; i++){
        data.push([districtData.features[i].precinctID, "WHITE", districtData.features[i].demProperties.populations["WHITE"], String(Math.floor(((districtData.features[i].demProperties.populations["WHITE"] / districtData.features[i].demProperties.totalPopulation)*10000))/100) + "%"]);
        for(var j = 0; j < races.length; j++){
            if(races[j] == "WHITE") continue;
            if(races[j] in districtData.features[i].demProperties.populations)
                data.push([districtData.features[i].precinctID, races[j], districtData.features[i].demProperties.populations[races[j]], String(Math.floor(((districtData.features[i].demProperties.populations[races[j]] / districtData.features[i].demProperties.totalPopulation)*10000))/100) + "%"]);
            else
                data.push([districtData.features[i].precinctID, races[j], 0, 0]);
        }
    }
    return data;
}

function displayDistrictPopTable(dataSet){
    districtTable = $('#district-pop-race-table').DataTable({
        data: dataSet,
        columns: [
            {title: "ID"},
            {title: "Race"},
            {title: "Total<br>Population"},
            {title: "Percentage"}
        ],
    });

    var dimension_col = null;
    var columnCount = $("#district-pop-race-table tr:first th").length;

    for (dimension_col = 0; dimension_col < columnCount; dimension_col++) {
        // first_instance holds the first instance of identical td
        var first_instance = null;
        var rowspan = 1;

        // iterate through rows
        $("#district-pop-race-table").find('tr').each(function () {

            // find the td of the correct column (determined by the dimension_col set above)
            var dimension_td = $(this).find('td:nth-child(' + dimension_col + ')');

            if (first_instance == null) {
                // must be the first row
                first_instance = dimension_td;
            } else if (dimension_td.text() == first_instance.text()) {
                // the current td is identical to the previous
                // remove the current td
                dimension_td.remove();
                ++rowspan;
                // increment the rowspan attribute of the first instance
                first_instance.attr('rowspan', rowspan);
            } else {
                // this cell is different from the last
                first_instance = dimension_td;
                rowspan = 1;
            }
        });
    }


    document.getElementsByClassName("table")[0].getElementsByTagName("h1")[3].style.display = "block";
    document.getElementById("district-pop-race-table").style.display = "table";
    phase1TableStatus = true;
    districtTable.draw();
}

function clearTable2(){
    phase1TableStatus = false;
    stateTable.destroy();
    districtTable.destroy();
    document.getElementById("state-pop-race-table").style.display = "none";
    document.getElementById("district-pop-race-table").style.display = "none";
    document.getElementsByClassName("table")[0].getElementsByTagName("h1")[2].style.display = "none";
    document.getElementsByClassName("table")[0].getElementsByTagName("h1")[3].style.display = "none";
}

function constructPhase2TableData(){
    var isNewDataMore = false;
    var tableData = [];
    if(newDistricts.length >= oldDistricts.length){
        isNewDataMore = true;
    }
    if(isNewDataMore){
        for(var i = 0; i < newDistricts.length; i++) {
            if (i <= oldDistricts.length - 1)
                tableData.push([oldDistricts[i].id, oldDistricts[i].gerryManderScore, newDistricts[i].id, newDistricts[i].gerryManderScore]);
            else
                tableData.push(["N/A", "N/A", newDistricts[i].id, newDistricts[i].gerryManderScore]);
        }
    }else{
        for(var i = 0; i < oldDistricts.length; i++) {
            if (i <= newDistricts.length - 1)
                tableData.push([oldDistricts[i].id, oldDistricts[i].gerryManderScore, newDistricts[i].id, newDistricts[i].gerryManderScore]);
            else
                tableData.push([oldDistricts[i].id, oldDistricts[i].gerryManderScore, "N/A", "N/A",]);
        }
    }
    return tableData;
}


function displayPhase2Table(dataSet){
    document.getElementById("phase2").style.display = "block";
    document.getElementById("phase2-table").style.display = "table";

    phase2Table = $('#phase2-table').DataTable({
        data: dataSet,
        columns: [
            {title: "Old ID"},
            {title: "Old Score"},
            {title: "New ID"},
            {title: "New Score"}
        ],
    });
    phase2TableStatus = true;
    phase2Table.draw();

}

function clearTable3(){
    phase2Table.destroy();
    document.getElementById("phase2-table").style.display = "none";
    phase2TableStatus = false;
}


