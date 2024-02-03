var selectedPlayersHidden = document.getElementById("selectedPlayersHidden");
var selectedHomePlayersItemList = document.getElementById("selectedHomePlayersList");
var selectedAwayPlayersItemList = document.getElementById("selectedAwayPlayersList");

var homePlayers = []
var awayPlayers = []

function selectPlayer(id)
{
    selectedPlayersHidden.value += id + ','
}

function updateSelectedPlayers()
{
    selectedPlayersHidden.value = ''

    homePlayers.forEach(selectPlayer)
    awayPlayers.forEach(selectPlayer)

    selectedPlayersHidden.value = selectedPlayersHidden.value.replace(/,$/, '');
}

function updatePlayers(team)
{
    var selectedTeamId = document.getElementById(team).value;
    var availablePlayersList = []

    if(team == 'homeTeam')
    {
        availablePlayersList = document.getElementById("availableHomePlayersList");
        homePlayers = []
    }
    else
    {
        availablePlayersList = document.getElementById("availableAwayPlayersList");
        awayPlayers = []
    }

    // Clear the current list of available players
    availablePlayersList.innerHTML = '';

    // Fetch the players for the selected team using AJAX
    $.ajax({
        url: '/playersResponseBody/teams/' + selectedTeamId,
        type: 'GET',
        success: function(data) {
            // Populate the available players list
            data.forEach(function(player) {
                var newPlayerItem = document.createElement("li");
                newPlayerItem.innerHTML = '<input type="checkbox" class="playerCheckbox" value="' + player.id + '">' +
                '<span>' + player.name + '</span>';
                availablePlayersList.appendChild(newPlayerItem);
            });
        }
    });
}

function addSelectedPlayersToList(checkboxSelector, selectedPlayersItemList, selectedPlayers)
{
    while (selectedPlayersItemList.firstChild)
    {
        selectedPlayersItemList.removeChild(selectedPlayersItemList.firstChild);
    }

    $(checkboxSelector + ' .playerCheckbox:checked').each(function () {
        var playerId = $(this).val();
        var playerName = $(this).closest('li').find('span').text();

        // Create a new list item for the selected player
        var newPlayerItem = document.createElement("li");
        newPlayerItem.innerHTML = '<span>' + playerName + '</span>' +
        '<input type="hidden" name="selectedPlayersIds" value="' + playerId + '">' +
        '<button type="button" data-player-id="' + playerId + '" onclick="removeSelectedPlayer(this)">Remove</button>';

        // Append the new player item to the selected players list
        selectedPlayersItemList.appendChild(newPlayerItem);

        selectedPlayers.push(playerId)
    });
}


function addHomePlayers()
{
    addSelectedPlayersToList('#availableHomePlayersList', selectedHomePlayersItemList, homePlayers);

    updateSelectedPlayers();
}

function addAwayPlayers()
{
    addSelectedPlayersToList('#availableAwayPlayersList', selectedAwayPlayersItemList, awayPlayers);

    updateSelectedPlayers();
}


function removeSelectedPlayer(button)
{
    var playerItem = button.parentElement;
    var playerId = button.getAttribute('data-player-id')

    homePlayers = homePlayers.filter(function(item)
    {
        return item !== playerId
    })

    awayPlayers = awayPlayers.filter(function(item)
    {
        return item !== playerId
    })

    playerItem.remove();

    updateSelectedPlayers();
}

document.addEventListener('DOMContentLoaded', function()
{
    addHomePlayers();
    addAwayPlayers();
    updateSelectedPlayers();
});