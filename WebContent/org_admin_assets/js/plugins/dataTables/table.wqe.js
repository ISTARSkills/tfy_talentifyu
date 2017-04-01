
/*
 * Editor client script for DB table wqe
 * Created by http://editor.datatables.net/generator
 */

(function($){

$(document).ready(function() {
	var editor = new $.fn.dataTable.Editor( {
		ajax: 'php/table.wqe.php',
		table: '#wqe',
		fields: [
			{
				"label": "qwrqw:",
				"name": "qwrqw"
			},
			{
				"label": "qwrqw:",
				"name": "qwrqw"
			},
			{
				"label": "qwrqwr:",
				"name": "qwrqwr"
			}
		]
	} );

	var table = $('#wqe').DataTable( {
		dom: 'Bfrtip',
		ajax: 'php/table.wqe.php',
		columns: [
			{
				"data": "qwrqw"
			},
			{
				"data": "qwrqw"
			},
			{
				"data": "qwrqwr"
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

