
/*
 * options:{
 * 	applyTo: #id/.class
 *  numOfRows: int
 * 	numOfCols: int
 * 	data: [[]]	
 *	widthOfCell: int 
 * 	heightOfCell: int
 * }
 */
Table = function(options){
	var _numOfRows;
	var _numOfCols;
	if(options.data){
		_numOfRows = options.data.length;
		_numOfCols = options.data[0].length;
	}else{
		_numOfRows = options.numOfRows || 1;
		_numOfCols = options.numOfCols || 4;
	}
	var widthOfCell = options.widthOfCell || 150;
	var heightOfCell = options.heightOfCell || 20;
	var model = new TableModel(_numOfRows, _numOfCols);
	if(options.data){
		model.setData(options.data);
	}
	var applyTo = options.applyTo;
	
	
	var tableId = Table.generateId();
	var update = function(){
		var container = $(applyTo);
		
		container.empty();
		
		var html = [];
		//hidden textarea
		html.push("<textarea style='position:absolute; z-index:-1; left:-999px;' id='"+tableId+"_buf'></textarea>")
		
		//actions
		html.push("<div>");
		html.push("<div class='orders copyBtn'><span class='copyAll'></span>拷贝</div>");
		html.push("<div class='orders'><span class='clearAll'></span>清空</div>");
		html.push("<div class='orders'><span class='appendRow'></span>新增一行</div>");
		html.push("<div class='orders'><span class='appendCol'></span>新增一栏</div>");
		html.push("</div>");
		
		html.push("<table cellspacing='0' cellpadding='0' class='sTable' id='"+tableId+"'>");
		html.push("\t<tr>");// first row
			for (var i=0; i <= model.getColumnCount(); i++) {
				if(i == 0){
					html.push("\t\t<td></td>\t");
				}else{
				  	html.push("\t\t<td>&nbsp;"+i+" <span class='addCol'></span><span class='delCol'></span></td>\t");
				}
			};
			html.push("\t\t<td></td>\t");
		html.push("\t</tr>");
		for (var i=1; i <= model.getRowCount(); i++) {
			html.push("\t<tr>");
			html.push("\t\t<td width='30' align='right'>"+i+"&nbsp;</td>\t");
			for (var j=1; j <= model.getColumnCount(); j++) {
			  	html.push("\t\t<td><input class='cell'></td>\t");
			};
			//last column
			html.push("\t\t<td width='50'>&nbsp;<span class='addRow'></span><span class='delRow'></span></td>\t");
			html.push("\t</tr>");
		};
		html.push("</table>");
		container.append(html.join("\n"));
		
		//set values
		var dataModel = model.getColumnCount();
		$(applyTo + " .cell").change(key).each(function(idx, item){
			var col = idx % dataModel;
			var row = (idx - col) / dataModel;
			item.value = model.getValueAt(row, col) || "";
		});
		
		//css
		$(applyTo + " tr:first td:gt(0):lt("+model.getColumnCount()+")").attr("width", widthOfCell);
		$(applyTo + " tr").attr("height", heightOfCell);
		$(applyTo + " .copyAll").addClass("ui-icon").addClass("ui-icon-copy").parent();
		$(applyTo + " .clearAll").addClass("ui-icon").addClass("ui-icon-close").parent().click(clearAll);
		$(applyTo + " .appendRow").addClass("ui-icon").addClass("ui-icon-plus").parent().click(appendRow);
		$(applyTo + " .appendCol").addClass("ui-icon").addClass("ui-icon-plus").parent().click(appendColumn);
		
		
		$(applyTo + " .addRow").addClass("ui-icon").addClass("ui-icon-plus").click(addRow);
		$(applyTo + " .addCol").addClass("ui-icon").addClass("ui-icon-plus").click(addCol);
		$(applyTo + " .delRow").addClass("ui-icon").addClass("ui-icon-minus").click(delRow);
		$(applyTo + " .delCol").addClass("ui-icon").addClass("ui-icon-minus").click(delCol);
		if(model.hasHead()){
			$(applyTo + " tr:eq(1) input").css("background-color", "#eeeeee");
		}
		
		//clipboard
		var clip = new ZeroClipboard( $(".copyBtn"), {
			moviePath: "/js/ZeroClipboard.swf"
		});
		clip.on( 'dataRequested', function (client, args) {
			var text = model.getTextString();
			client.setText(text);
		});
		
		
		var buf = $("#" + tableId + "_buf");
		var pasteCell;
		$(applyTo + " .cell").keydown(function(evt){
			if(evt.ctrlKey && evt.keyCode == 86){
				pasteCell = $(this);
				buf.focus();
			}
		});
		buf.on("paste", function(evt){
			pasteCell.val("");
			$(this).one("keyup", {src:pasteCell}, finishPaste);
		})
		
	}
	
	var finishPaste = function(evt){
		var tgt = $(this);
		tgt.blur();
		var src = evt.data.src;
		populate(tgt.val(), src.parent().parent().index(), src.parent().index());
	}
	
	var key = function(){
		var src = $(this);
		model.setValueAt(src.val(), src.parent().parent().index() - 1, src.parent().index() - 1);
	}
	
	
	var populate = function(data, rowIndex, columnIndex){
		data = data.replace("/\r\n/g", "\n");
		var cells = $(applyTo + " .cell");
		var dataOfRows = data.split("\n");
		if(dataOfRows[dataOfRows.length - 1] == ""){
			dataOfRows.pop();
		}
		
		var endRow = (dataOfRows.length - 1) + rowIndex;
		var rowCount = model.getRowCount();
		if(rowCount < endRow){
			endRow = rowCount;
		}
		for (var i=rowIndex; i <= endRow; i++) {
			var dataOfCells = dataOfRows[i - rowIndex].split("\t");
			if(dataOfCells[dataOfCells.length - 1] == ""){
				dataOfCells.pop();
			}
			var endCell = (dataOfCells.length - 1) + columnIndex;
			var colCount = model.getColumnCount();
			if(colCount < endCell){
				endCell = colCount;
			}
			for (var j=columnIndex; j <= endCell; j++){
				var index = (i - 1)* colCount + (j - 1);
				var v = dataOfCells[j - columnIndex];
				cells[index].value = v;
				model.setValueAt(v, i - 1, j - 1);	
			}
		}
	}
	
	var clearAll = function(){
		model.clear();
		update();
	}
	
	var addRow = function(){
		var rowIndex = $(this).parent().parent().index() - 1;
		model.addRow(rowIndex);
		update();
	}
	var delRow = function(){
		var rowIndex = $(this).parent().parent().index() - 1;
		model.removeRow(rowIndex);
		update();
	}
	var addCol = function(){
		var colIndex = $(this).parent().index() - 1;
		model.addColumn(colIndex);
		update();
	}
	var delCol = function(){
		var colIndex = $(this).parent().index() - 1;
		model.removeColumn(colIndex);
		update();
	}
	
	var appendColumn = function(){
		model.appendColumn();
		update();
	}
	
	var appendRow = function(){
		model.appendRow();
		update();
	}
	
	this.getValue = function(){
		return model.getTextString();
	}
	
	$(update);
}
Table.sequence = 0;
Table.generateId = function(){
	return "t" + Table.sequence ++;
}

/*
 * 
 */
TableModel = function(numOfRows, numOfCols){
	var hasHead = false;
	var cells;
	var init = function(numOfRows, numOfCols){
		hasHead = false;
		buildData(numOfRows, numOfCols);
	}
	
	var buildData = function(numOfRows, numOfCols){
		cells = new Array(numOfRows);
		for (var i=0; i < cells.length; i++) {
		  	cells[i] = new Array(numOfCols);
		};
	}
	
	this.setData = function(values){
		hasHead = false;
		if(!values.length){
			alert("invalid data");
		}
		else{
			if(values[0] instanceof Array){
				//[[]]
				cells = values;
			}else{
				//json object
				hasHead = true;
				cells = [];
				var firstRow = [];
				for(var x in values[0]){
					firstRow.push(x);
				}
				cells.push(firstRow);
				for(var i=0; i<values.length; i++){
					var row = [];
					var obj = values[i];
					for(var j=0; j<firstRow.length; j++){
						row.push(obj[firstRow[j]]);
					}
					cells.push(row);
				}
			}
		}
	}
	
	this.hasHead = function(){
		return hasHead;
	}
	
	this.getData = function(){
		return cells;
	}
	
	this.setValueAt = function(aValue, rowIndex, columnIndex) {
		cells[rowIndex][columnIndex] = aValue;
	}
	
	this.getValueAt = function(rowIndex, columnIndex){
		return cells[rowIndex][columnIndex];
	}
	
	this.addRow = function(rowIndex){
		if(rowIndex == 0){
			hasHead = false;
		}
		var temp = new Array(this.getColumnCount());
		cells.splice(rowIndex, 0, temp);
	}
	
	this.appendRow = function(){
		var temp = new Array(this.getColumnCount());
		cells.push(temp);
	}
	
	this.removeRow = function(rowIndex){
		if(rowIndex == 0){
			hasHead = false;
		}
		return cells.splice(rowIndex, 1);
	}
	
	this.addColumn = function(colIndex){
		for (var i=0; i < cells.length; i++) {
		  	cells[i].splice(colIndex, 0, "");
		};
	}
	
	this.appendColumn = function(){
		for (var i=0; i < cells.length; i++) {
		  	cells[i].push("");
		};
	}
	
	this.removeColumn = function(colIndex){
		var col = [];
		for (var i=0; i < cells.length; i++) {
		  	col.push(cells[i].splice(colIndex, 1));
		};
		return col;
	}
	
	this.getRowCount = function(){
		return cells.length || 0;
	}
	this.getColumnCount = function(){
		if(cells[0])
			return cells[0].length;
		return 0;
	}
	
	this.clear = function(){
		init(this.getRowCount(), this.getColumnCount());
	}
	
	this.getTextString = function(){
		var d = cells;
		var output = [];
		for (var i=0; i < d.length; i++) {
		   var r = d[i];
		   output.push(r.join("\t"));
		};
		var text = output.join("\n");
		return text;
	}
	
	init(numOfRows, numOfCols);
}
