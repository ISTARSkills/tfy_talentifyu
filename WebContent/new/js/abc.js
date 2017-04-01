var editor;
editor = new $.fn.dataTable.Editor({
	ajax : '/edit_data?report_id=198&org_id=8',
	idSrc : 'id',
	table : '#datatable_report_198',
	fields : [ {
					label : "Student Id:",
					name : "id",
					type : "readonly"
					},{
						label : "Email:",
					name : "email"
					},{
						label : "Name:",
					name : "name"
					},{
						label : "Gender:",
					name : "gender",
					type : "radio",
					options : [ {
						label : "MALE",
						value : "MALE"
					}, {
						label : "FEMALE",
						value : "FEMALE"
						}, ],
					},{
						label : "Password:",
					name : "password"
					},{
						label : "mobile:",
					name : "mobile"
					},
		]
});

$('#datatable_report_198').on('click', 'tbody td:not(:first-child)',
		function(e) {
			editor.inline(this);
});

$('#datatable_report_198').DataTable({
	ajax : '/data?report_id=198&org_id=8',
	responsive : true,
	stateSave : true,
	"pageLength" : 50,
	"oLanguage" : {
		"sSearch" : '<i class="fa fa-search"></i>'
	},
	dom : 'Bfrtip',
	buttons : [ 'copy', 'csv', 'excel', 'pdf', 'print', {
		extend : "create",
		editor : editor
	}, {
		extend : "edit",
		editor : editor
	} ],

	select : {
		style : 'os',
		selector : 'td:first-child'
	},

	columns : [ {
		data : null,
		defaultContent : '',
		className : 'select-checkbox',
		orderable : false
		},{
			data : "id"
		},{
			data : "email"
		},{
			data : "name"
		},{
			data : "gender"
		},{
			data : "password"
		},{
			data : "mobile"
		},
	],
});
