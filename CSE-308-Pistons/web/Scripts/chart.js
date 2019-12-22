var statePieChart;
var stateBarChart; // stateVote
var pieChart;
var barChart;

function initChart(){
    var ken_pop = 4285828;
    var lou_pop = 4668546;
    var tex_pop = 28715478;
    var data = new google.visualization.arrayToDataTable([
        ['States', 'Population'],
        ['Kentucky', ken_pop],
        ['Louisiana', lou_pop],
        ['Texas', tex_pop],
    ]);

    var options = {
        'title': 'Population Of States',
        'width': 250,
        'height': 300,
        backgroundColor: {
            fill: 'white',
            fillOpacity: 0.8
        },
        titleTextStyle: {
            fontSize: 14
        },
        chart: {
            title: 'Population by State',
        },
        bars: 'horizontal', // Required for Material Bar Charts.
        axes: {
            x: {
                0: {side: 'top', label: 'Percentage'} // Top x-axis.
            }
        },
        bar: {groupWidth: "90%"}
    };

    var options2 = {
        'title': 'Population Of States',
        'width': 250,
        'height': 300,
        titleTextStyle: {
            fontSize: 14
        },
        legend:{
            textStyle:{
                fontsize: 10
            }
        },
        is3D: true,
        backgroundColor: {
            fill: 'white',
            fillOpacity: 0.8
        },
    };

    stateBarChart = new google.visualization.BarChart(document.getElementById('stateBarChart'));
    stateBarChart.draw(data, options);

    // Display the chart inside the <div> element with id="piechart"
    statePieChart = new google.visualization.PieChart(document.getElementById('statePieChart'));
    statePieChart.draw(data, options2);

    // Instantiate and draw our chart, passing in some options.
    pieChart = new google.visualization.PieChart(document.getElementById('pieChart'));
    barChart = new google.visualization.BarChart(document.getElementById('barChart'));

    pieChart.draw(data, options2);
    barChart.draw(data, options);

    pieChart.clearChart();
    barChart.clearChart();
}
function isElectionTypeSelected(){
    var selectedYear = document.getElementById("votingYearDropDown").value;
    var selectedVotingType = document.getElementById("votingType").value;
    if (selectedYear == "Default" || selectedVotingType == "Default") {
        // alert user to select Year
        return false;
    }else{
        return true;
    }
}
function displayStateDemo(){
    var stateDemographicData = statesData2.demographic;

    var demoData = [];
    demoData[0] = statesData2.stateName + " Demographics";
    demoData[1] = stateDemographicData.whitePop;
    demoData[2] = stateDemographicData.africanAmericanPop;
    demoData[3] = stateDemographicData.asianPop;
    demoData[4] = stateDemographicData.americanIndianPop;
    demoData[5] = stateDemographicData.othersPop;

    drawDemoData(demoData,true);
}

function drawStateElectionChart(){
    if(!isElectionTypeSelected()){
        return;
    }
    var stateElectionData =  statesData2.electionData;
    var electionData = [];
    var selectedYear = document.getElementById("votingYearDropDown").value;
    var selectedVotingType = document.getElementById("votingType").value;
    if (selectedYear == 2016 && selectedVotingType == "Presidential") {
        electionData[0] = "Presidential Votes 2016";
        electionData[1] = stateElectionData.presRep2016;
        electionData[2] = stateElectionData.presDem2016;
        electionData[3] = stateElectionData.presOthers2016;
    } else if (selectedYear == 2016 && selectedVotingType == "Congressional") {
        electionData[0] = "Congressional Votes 2016";
        electionData[1] = stateElectionData.congressionalRep2016;
        electionData[2] = stateElectionData.congressionalDem2016;
        electionData[3] = stateElectionData.congressionalOthers2016;
    } else {
        // 2018 Congressional
        electionData[0] = "Congressional Votes 2018";
        electionData[1] = stateElectionData.congressionalRep2018;
        electionData[2] = stateElectionData.congressionalDem2018;
        electionData[3] = stateElectionData.congressionalOthers2018;
    }
    drawElectionData(electionData,true)
}
function drawVotingChart(feature) {
   if(!isElectionTypeSelected()){
       return;
   }
    var selectedYear = document.getElementById("votingYearDropDown").value;
    var selectedVotingType = document.getElementById("votingType").value;
    var electionData = [];
    if(phase1Btn){
        if (selectedYear == 2016 && selectedVotingType == "Presidential") {
            electionData[0] = "Presidential Votes 2016";
            electionData[1] = feature.phase1.electionProperties.presRep2016;
            electionData[2] = feature.phase1.electionProperties.presDem2016;
            electionData[3] = feature.phase1.electionProperties.presOthers2016;
        } else if (selectedYear == 2016 && selectedVotingType == "Congressional") {
            electionData[0] = "Congressional Votes 2016";
            electionData[1] = feature.phase1.electionProperties.congressionalRep2016;
            electionData[2] = feature.phase1.electionProperties.congressionalDem2016;
            electionData[3] = feature.phase1.electionProperties.congressionalOthers2016;
        } else {
            // 2018 Congressional
            electionData[0] = "Congressional Votes 2018";
            electionData[1] = feature.phase1.electionProperties.congressionalRep2018;
            electionData[2] = feature.phase1.electionProperties.congressionalDem2018;
            electionData[3] = feature.phase1.electionProperties.congressionalOthers2018;
        }
    }else{
        if (selectedYear == 2016 && selectedVotingType == "Presidential") {
            electionData[0] = "Presidential Votes 2016";
            electionData[1] = feature.electionProperties.presRep2016;
            electionData[2] = feature.electionProperties.presDem2016;
            electionData[3] = feature.electionProperties.presOthers2016;
        } else if (selectedYear == 2016 && selectedVotingType == "Congressional") {
            electionData[0] = "Congressional Votes 2016";
            electionData[1] = feature.electionProperties.congressionalRep2016;
            electionData[2] = feature.electionProperties.congressionalDem2016;
            electionData[3] = feature.electionProperties.congressionalOthers2016;
        } else {
            // 2018 Congressional
            electionData[0] = "Congressional Votes 2018";
            electionData[1] = feature.electionProperties.congressionalRep2018;
            electionData[2] = feature.electionProperties.congressionalDem2018;
            electionData[3] = feature.electionProperties.congressionalOthers2018;
        }
    }
    drawElectionData(electionData,false)
}

function drawElectionData(electionData, displayStateData){
    var data = google.visualization.arrayToDataTable([
        ["Party", "Votes", {role: "style"}],
        ["Republican", electionData[1], "red"],
        ["Democrat", electionData[2], "blue"],
        ["Others", electionData[3], "orange"],
    ]);
    var view = new google.visualization.DataView(data);
    view.setColumns([0, 1,
        {
            calc: "stringify",
            sourceColumn: 1,
            type: "string",
            role: "annotation"
        },
        2]);
    var options = {
        title: electionData[0],
        width: 250,
        height: 300,
        titleTextStyle: {
            fontSize: 14
        },
        bar: {groupWidth: "50%"},
        legend: {position: "none"},
        backgroundColor: {
            fill: 'white',
            fillOpacity: 0.8
        },
    };
    if(displayStateData){
        stateBarChart = new google.visualization.BarChart(document.getElementById('stateBarChart'));
        stateBarChart.draw(view, options);
    }else{
        barChart = new google.visualization.BarChart(document.getElementById('barChart'));
        barChart.draw(view, options);
    }

}
function displayDemo(feature){
    console.log(feature);
    var demoData = [];
    demoData[0] = "Demographics";
    if(phase1Btn){
        demoData[1] = feature.phase1.demProperties.populations.WHITE;
        demoData[2] = feature.phase1.demProperties.populations.AFRICAN_AMERICANS;
        demoData[3] = feature.phase1.demProperties.populations.ASIAN;
        demoData[4] = feature.phase1.demProperties.populations.AMERICAN_INDIAN;
        demoData[5] = feature.phase1.demProperties.populations.OTHERS;
        console.log(demoData[2]);
    }else{
        demoData[1] = feature.demProperties.whitePop;
        demoData[2] = feature.demProperties.africanAmericanPop;
        demoData[3] = feature.demProperties.asianPop;
        demoData[4] = feature.demProperties.americanIndianPop;
        demoData[5] = feature.demProperties.othersPop;
    }


    drawDemoData(demoData,false)
}
function drawDemoData(demoData, displayStateData){
    var data = google.visualization.arrayToDataTable([
        ['Race', 'Population'],
        ['White', demoData[1]],
        ['Black', demoData[2]],
        ['Asian', demoData[3]],
        ['Native American', demoData[4]],
        ['Other', demoData[5]]
    ]);
    var view = new google.visualization.DataView(data);

    var options = {
        'title': demoData[0],
        'width': 250,
        'height': 300,
        titleTextStyle: {
            fontSize: 14
        },
        legend:{
            textStyle:{
                fontsize: 10
            }
        },
        is3D: true,
        backgroundColor: {
            fill: '#f9f9f9',
            fillOpacity: 0.8
        },
        slices: {
            3: {offset: 0.3},
            4: {offset: 0.4},
            5: {offset: 0.5},
            6: {offset: 0.6},
            7: {offset: 0.7},
        }
    };
    // Instantiate and draw our chart, passing in some options.
    if(displayStateData){
        statePieChart = new google.visualization.PieChart(document.getElementById('statePieChart'));
        statePieChart.draw(view, options);
    }else {
        pieChart = new google.visualization.PieChart(document.getElementById('pieChart'));
        pieChart.draw(view, options);
    }
}

