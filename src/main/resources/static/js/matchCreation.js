function updatePlayers()
{
    var selectedTeamId = document.getElementById("homeTeam").value;
    var availablePlayersList = document.getElementById("availablePlayersList");

    // Clear the current list of available players
    availablePlayersList.innerHTML = '';

    // Fetch the players for the selected team using AJAX
    $.ajax({
        url: '/teams/' + selectedTeamId + '/playersResponseBody',
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


// Function to add all selected players to the selected players list
function addAllPlayers()
{
    var selectedPlayersList = document.getElementById("selectedPlayersList");
    var selectedPlayersHidden = document.getElementById("selectedPlayersHidden");

    // Clear previous selected player IDs
    selectedPlayersHidden.value = '';

    // Find all checkboxes in the available players list
    $('#availablePlayersList .playerCheckbox:checked').each(function ()
    {
        var playerId = $(this).val();
        var playerName = $(this).closest('li').find('span').text();

        // Create a new list item for the selected player
        var newPlayerItem = document.createElement("li");
        newPlayerItem.innerHTML = '<span>' + playerName + '</span>' +
        '<input type="hidden" name="selectedPlayersIds" value="' + playerId + '">' +
        '<button type="button" onclick="removeSelectedPlayer(this)">Remove</button>';

        // Append the new player item to the selected players list
        selectedPlayersList.appendChild(newPlayerItem);

        // Update the hidden input field with selected player IDs
        selectedPlayersHidden.value += playerId + ',';
    });

    // Remove the trailing comma
    selectedPlayersHidden.value = selectedPlayersHidden.value.replace(/,$/, '');
}


function removeSelectedPlayer(button)
{
    var playerItem = button.parentElement;
    playerItem.remove();
}