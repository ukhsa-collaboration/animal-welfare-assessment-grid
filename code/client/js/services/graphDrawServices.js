var graphServices = angular.module('graphDrawServices', []);
graphServices.factory('graphDrawService', [function() {

    var drawTimeline = function(element, data, options, fnOnClick, fnOnHover) {



        var iso = d3.time.format.utc("%Y-%m-%dT%H:%M:%S.%LZ").parse;

        var width = options.dimension.width - options.margin.left - options.margin.right;
        
        var height = options.dimension.height - options.margin.top - options.margin.bot;

        var center = Math.round(width/2);

        var that = this;

        //transform the date to something d3 can understand only if it hasnt already
        angular.forEach(data, function(value, key) {
            if(typeof(value.date) ==="string")
            {
                value.date = iso(value.date);
            }
        });

        //create the date and score scales
        var xScale = d3.time.scale().range([0, width]);
        xScale.domain(d3.extent(data, function(d) { 
            return d.date; 
        }));
        var yScale = d3.scale.linear().range([height, 0]);
        yScale.domain([0, d3.max(data, function(d) {
             return d.cwas;
        })]);

        //create the axes
        var xAxis = d3.svg.axis()
            .scale(xScale)
            .orient("bottom")
            .innerTickSize(-height)
            .outerTickSize(0)
            .tickPadding(10);

        var yAxis = d3.svg.axis()
            .scale(yScale)
            .orient("left")
            .innerTickSize(-width)
            .outerTickSize(0)
            .tickPadding(10);

        //create the line object
        var valueline = d3.svg.line()
            .x(function(d) { 
                return xScale(d.date); 
            })
            .y(function(d) { 
                return yScale(d.cwas); 
            })
            .interpolate("basic");

        //create the svg element
        var svg = d3.select(element)
            .append("svg")
            .attr("width", width + options.margin.left + options.margin.right)            
            .attr("height", height + options.margin.top + options.margin.bot)
            .append("g")
            .attr("transform", "translate(" + options.margin.left + "," + options.margin.top + ")");
        
        // Add the valueline path.
        svg.append("path")  
            .attr("class", "line")
            .attr("d", valueline(data));
 
        // Add the X Axis
        svg.append("g")     
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll("text")
            .style(function(){
                return {
                    "text-anchor" : "start",
                    "font-size" : "20px"
                }
            })
            .attr("transform", /*"rotate(45)"*/function(d,i){
 /*               console.log(this);
                console.log(d);
                console.log(i);
                console.log(jQuery(this).width());
                console.log(width);
                console.log(height);*/
            });
 
        // Add the Y Axis
        svg.append("g")     
            .attr("class", "y axis")
            .call(yAxis);

        //add axes labels
        svg.append("text")
            .attr("class", "x label")
            .attr("text-anchor", "end")
            .attr("x", width)
            .attr("y", height - 6)
            .text("Date");

        svg.append("text")
            .attr("class", "y label")
            .attr("text-anchor", "end")
            .attr("y", 10)
            .attr("dy", ".75em")
            .attr("transform", "rotate(-90)")
            .attr("font-size", function(){
               //TODO: resize text
            })
            .text(function(){
                if(height > 260) {
                    return "Cumulative welfare assessment score (CWAS)";
                } else {
                    return "CWAS";
                }                
            });

        var pointLabels = svg.selectAll("rect")
            .data(data)
            .enter()
            .append("text")
            .attr("id", function(d,i){
                return "textLabel" + i;
            })
            .attr("x", function(d){
                return xScale(d.date);
            })
            .attr("y", function(d){
                return yScale(d.cwas);
            })
            .attr("text-anchor", function(d, i){
                var midPoint = Math.round(data.length/2);

                if(i <= midPoint){
                     return "start";
                }
                return "end";
            })
            .text(function(d){
                return Math.round(d.cwas * 100)/100;
            })            
            .classed("dataLabelHidden", true);

        
        //create circles points and mouseover
        var points = svg.selectAll("circle")
            .data(data)
            .enter()
            .append("circle").attr({
                cx: function(d) {
                    return xScale(d.date);
                },
                cy: function(d) {
                    return yScale(d.cwas);
                },
                r: function(){
                    return 5;
                }
            })
            .classed("pointCircle", true)
            .on("mouseover", function(d,i) {
                fnOnHover(d);
                var textLabel = d3.selectAll("#textLabel" + i);
                textLabel.classed("dataLabelHidden", false);
                textLabel.classed("dataLabel", true);
            }).on("click", function(d) {
                fnOnClick(d);
            }).on("mouseout", function(d,i){
                var textLabel = d3.selectAll("#textLabel" + i);
                textLabel.classed("dataLabelHidden", true);
                textLabel.classed("dataLabel", false);
            });
    };

    var removeSvg = function(element){
        var svg = d3.select(element);
        svg.remove();
    };

    var drawRadarChart = function(element, data, options, onclick) {
        var chart = RadarChart.chart();
        var config = chart.config();
        config.maxValue = 10;
        config.levels = 10;
        config.factorLegend = 1;
        config.w = options.dimension.width;
        config.h = options.dimension.width;
        config.factor = 0.8;
        config.onclick = onclick;
        RadarChart.draw(element, data, config);
    };
    
    return {
        removeSvg : removeSvg,
        drawTimeline: drawTimeline,
        drawRadarChart: drawRadarChart
    }
}]);