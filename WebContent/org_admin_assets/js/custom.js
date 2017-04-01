function EleMove() {
    var element = ".ui-sortable";
	var handle = ".ibox-title";
	var connect = ".ui-sortable";
	$(element).sortable({
		handle : handle,
		connectWith : connect,
		tolerance : 'pointer',
		opacity : 0.8
	}).disableSelection();
}
