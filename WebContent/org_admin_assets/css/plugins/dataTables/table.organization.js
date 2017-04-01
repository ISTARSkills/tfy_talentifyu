
/*
 * Editor client script for DB table organization
 * Created by http://editor.datatables.net/generator
 */

(function($){

$(document).ready(function() {
	var editor = new $.fn.dataTable.Editor( {
		ajax: 'php/table.organization.php',
		table: '#organization',
		fields: [
			{
				"label": "name:",
				"name": "name"
			}
		]
	} );

	var table = $('#organization').DataTable( {
		dom: 'Bfrtip',
		ajax: 'php/table.organization.php',
		columns: [
			{
				"data": "name"
			}
		],
		select: true,
		lengthChange: false,
		buttons: [
			{ extend: 'create', editor: editor },
			{ extend: 'edit',   editor: editor },
			{ extend: 'remove', editor: editor }
		]
	} );
} );

}(jQuery));

