/**
 * Created by MSI on 11.09.2017.
 */
var ajaxUrl = "ajax/meals/";
var datatableApi;
var startDate;
var endDate;
var startTime;
var endTime;

// $(document).ready(function () {
$(function () {

    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});

function dropFilter() {
    startDate = null;
    endDate = null;
    startTime = null;
    endTime = null;
    getFiltered()
}
function filter() {

    if (document.getElementById("startTime").value !== "") {
        startTime = document.getElementById("startTime").value;
    }
    if (document.getElementById("endTime").value !== "") {
        endTime = document.getElementById("endTime").value;
    }
    if (document.getElementById("startDate").value !== "") {
        startDate = document.getElementById("startDate").value;
    }
    if (document.getElementById("endDate").value !== "") {
        endDate = document.getElementById("endDate").value;
    }
    getFiltered();
    successNoty("Filtered");
}
function getFiltered() {
    var data = {
        startTime: startTime,
        endTime: endTime,
        startDate: startDate,
        endDate: endDate
    };

    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: jQuery.param(data),
        dataType: "JSON",
        success: function (dataReap) {
            datatableApi.clear().rows.add(dataReap).draw();
        }
    });
}

function updateTable() {
    filter();
}

