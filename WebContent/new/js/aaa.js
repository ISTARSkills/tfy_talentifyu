$('#datatable_report_198').DataTable({
			ajax : '/data?report_id=198&org_id=8',
			responsive : true,
			stateSave : true,
			"pageLength" : 50,
			"oLanguage" : {
				"sSearch" : '<i class="fa fa-search"></i>'}, 
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
			
		}); 
