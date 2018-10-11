function populateData(drivers, url) {
  $.ajax({
    type: 'GET',
    url: url,
    success: function (data) {
      $.each(data, function (i, item) {
        drivers.append('<tr><td>' + item.name + '</td><td>' + item.points
            + '</td></tr>')
      });
    }
  });
}

jQuery(function () {
  var drivers = $('#drivers-table-body');
  var teams = $('#teams-table-body');
  populateData(drivers, 'http://localhost:8080/drivers');
  populateData(teams, 'http://localhost:8080/teams');
});

