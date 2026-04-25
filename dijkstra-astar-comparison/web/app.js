// Graph presets: minimal, small (10 nodes), large (15 nodes)
const presets = {
  minimal: {
    nodes: [
      {id: 'A', x: 80,  y: 80},
      {id: 'B', x: 320, y: 80},
      {id: 'C', x: 200, y: 180},
      {id: 'D', x: 80,  y: 320},
      {id: 'E', x: 320, y: 320}
    ],
    edges: [
      ['A','B',2.0],['A','C',1.5],['B','C',1.0],['C','D',2.0],['C','E',2.0],['D','E',3.0]
    ]
  },
  small: {
    // MapFactory.buildSmallCityMap() transcription
    nodes: [
      {id:'0', label:'A-Market', x:0, y:4}, {id:'1', label:'B-Library', x:2, y:4}, {id:'2', label:'C-School', x:4, y:4},
      {id:'3', label:'D-Hospital', x:0, y:2}, {id:'4', label:'E-Center', x:2, y:2}, {id:'5', label:'F-Park', x:4, y:2},
      {id:'6', label:'G-Station', x:0, y:0}, {id:'7', label:'H-Mall', x:2, y:0}, {id:'8', label:'I-Airport', x:4, y:0}, {id:'9', label:'J-Hotel', x:6, y:0}
    ],
    edges: [
      ['0','1',2.5],['1','2',2.5],['3','4',2.5],['4','5',2.5],['6','7',2.5],['7','8',2.5],['8','9',2.5],
      ['0','3',2.5],['3','6',2.5],['1','4',2.5],['4','7',2.5],['2','5',2.5],['5','8',2.5],
      ['1','3',3.5],['4','2',3.5]
    ]
  },
  large: {
    // MapFactory.buildLargerCityMap() transcription (ids 0..14)
    nodes: [
      {id:'0', label:'North-Gate', x:0, y:8},{id:'1', label:'University', x:3, y:8},{id:'2', label:'Tech-Park', x:6, y:8},{id:'3', label:'Airport', x:9, y:8},
      {id:'4', label:'Old-Town', x:0, y:5},{id:'5', label:'City-Hall', x:3, y:5},{id:'6', label:'Business-Dist', x:6, y:5},{id:'7', label:'East-Bridge', x:9, y:5},
      {id:'8', label:'Suburbs', x:0, y:2},{id:'9', label:'Mall', x:3, y:2},{id:'10', label:'Stadium', x:6, y:2},{id:'11', label:'Harbor', x:9, y:2},
      {id:'12', label:'South-Gate', x:0, y:0},{id:'13', label:'Train-Station', x:4, y:0},{id:'14', label:'Sea-Port', x:9, y:0}
    ],
    edges: [
      ['0','1',3.2],['1','2',3.2],['2','3',3.2],
      ['4','5',3.2],['5','6',3.2],['6','7',3.2],
      ['8','9',3.2],['9','10',3.2],['10','11',3.2],
      ['12','13',4.5],['13','14',5.5],
      ['0','4',3.2],['4','8',3.2],['8','12',2.5],
      ['1','5',3.2],['5','9',3.2],['9','13',2.5],
      ['3','7',3.2],['7','11',3.2],['11','14',2.5],
      ['2','6',3.2],['6','10',3.2],
      ['1','6',4.8],['5','10',4.8],['9','14',6.5]
    ]
  }
};

let nodes = presets.minimal.nodes.map(n=>({id:n.id, x:n.x*40+60, y:(8-(n.y||n.y))*40+40, label:n.label||n.id}));
let edges = presets.minimal.edges.map(e=>[e[0],e[1],e[2]]);

const svg = document.getElementById('graph');
const startSelect = document.getElementById('startSelect');
const goalSelect = document.getElementById('goalSelect');
const presetSelect = document.getElementById('presetSelect');
const runBothBtn = document.getElementById('runBoth');
const resetBtn = document.getElementById('resetBtn');
const resultsDiv = document.getElementById('results');

function idToNode(id){return nodes.find(n=>n.id===id)}

function setPreset(name){
  const p = presets[name];
  // scale and center positions for SVG
  nodes = p.nodes.map(n=>({id:n.id, x:(n.x||0)*40+60, y:(8-(n.y||0))*40+40, label:n.label||n.id}));
  edges = p.edges.map(e=>[e[0],e[1],e[2]]);
  populateSelects(); drawGraph();
}

function clearSvg(){while(svg.firstChild)svg.removeChild(svg.firstChild)}

function drawGraph(){
  clearSvg();
  // draw edges
  edges.forEach(([a,b,w])=>{
    const na=idToNode(a), nb=idToNode(b);
    const line = document.createElementNS('http://www.w3.org/2000/svg','line');
    line.setAttribute('x1',na.x);
    line.setAttribute('y1',na.y);
    line.setAttribute('x2',nb.x);
    line.setAttribute('y2',nb.y);
    line.setAttribute('class','edge');
    line.dataset.edge = `${a}-${b}`;
    svg.appendChild(line);

    // weight label
    const tx = (na.x+nb.x)/2, ty = (na.y+nb.y)/2;
    const text = document.createElementNS('http://www.w3.org/2000/svg','text');
    text.setAttribute('x',tx+6);
    text.setAttribute('y',ty-6);
    text.setAttribute('class','edge weight');
    text.textContent = w;
    svg.appendChild(text);
  });

  // draw nodes
  nodes.forEach(n=>{
    const g = document.createElementNS('http://www.w3.org/2000/svg','g');
    g.setAttribute('transform',`translate(${n.x},${n.y})`);
    const circle = document.createElementNS('http://www.w3.org/2000/svg','circle');
    circle.setAttribute('r',16);
    circle.setAttribute('class','node');
    circle.dataset.id = n.id;
    g.appendChild(circle);
    const label = document.createElementNS('http://www.w3.org/2000/svg','text');
    label.setAttribute('class','node label');
    label.setAttribute('x',0);
    label.setAttribute('y',0);
    label.textContent = n.id;
    g.appendChild(label);
    svg.appendChild(g);
  });
}

function buildAdj(){
  const adj = new Map();
  nodes.forEach(n=>adj.set(n.id,[]));
  edges.forEach(([a,b,w])=>{
    adj.get(a).push({to:b,w});
    adj.get(b).push({to:a,w});
  });
  return adj;
}

function dijkstra(start,goal){
  const adj = buildAdj();
  const dist = new Map();
  const prev = new Map();
  nodes.forEach(n=>dist.set(n.id,Infinity));
  dist.set(start,0);
  const pq = new Set(nodes.map(n=>n.id));
  let nodesExplored = 0;
  const t0 = performance.now();
  while(pq.size){
    // extract min
    let u=null, best=Infinity;
    pq.forEach(v=>{if(dist.get(v)<best){best=dist.get(v);u=v}});
    if(u===null)break;
    pq.delete(u);
    nodesExplored++;
    if(u===goal) break;
    adj.get(u).forEach(({to,w})=>{
      const alt = dist.get(u)+w;
      if(alt < dist.get(to)){
        dist.set(to,alt); prev.set(to,u);
      }
    });
  }
  const t1 = performance.now();
  const path=[]; let cur=goal;
  while(cur){path.unshift(cur); if(cur===start)break; cur=prev.get(cur); if(cur===undefined)break}
  if(path[0]!==start) return {path:[],totalCost:-1,nodesExplored,executionTimeNs:Math.round((t1-t0)*1e6)};
  const totalCost = computePathCost(path);
  return {path,totalCost,nodesExplored,executionTimeNs:Math.round((t1-t0)*1e6)};
}

function heuristic(a,b){
  const na=idToNode(a), nb=idToNode(b);
  const dx=na.x-nb.x, dy=na.y-nb.y; return Math.hypot(dx,dy);
}

function astar(start,goal){
  const adj = buildAdj();
  const open = new Set([start]);
  const cameFrom = new Map();
  const gScore = new Map();
  const fScore = new Map();
  nodes.forEach(n=>{gScore.set(n.id,Infinity); fScore.set(n.id,Infinity)});
  gScore.set(start,0); fScore.set(start,heuristic(start,goal));
  let nodesExplored = 0;
  const t0 = performance.now();
  while(open.size){
    let current=null, best=Infinity;
    open.forEach(v=>{if(fScore.get(v)<best){best=fScore.get(v);current=v}});
    if(current===goal) break;
    open.delete(current);
    nodesExplored++;
    adj.get(current).forEach(({to,w})=>{
      const tentative = gScore.get(current)+w;
      if(tentative < gScore.get(to)){
        cameFrom.set(to,current);
        gScore.set(to,tentative);
        fScore.set(to, tentative + heuristic(to,goal));
        open.add(to);
      }
    });
  }
  const t1 = performance.now();
  const path=[]; let cur=goal;
  while(cur){path.unshift(cur); if(cur===start)break; cur=cameFrom.get(cur); if(cur===undefined)break}
  if(path[0]!==start) return {path:[],totalCost:-1,nodesExplored,executionTimeNs:Math.round((t1-t0)*1e6)};
  const totalCost = computePathCost(path);
  return {path,totalCost,nodesExplored,executionTimeNs:Math.round((t1-t0)*1e6)};
}

function computePathCost(path){
  if(!path || path.length<2) return -1;
  let cost=0;
  for(let i=0;i<path.length-1;i++){
    const a=path[i], b=path[i+1];
    const e = edges.find(x=>(x[0]===a&&x[1]===b)||(x[0]===b&&x[1]===a));
    if(!e) return -1; cost += e[2];
  }
  return cost;
}

function formattedTime(ns){ return `${(ns/1000).toFixed(3)} µs`; }

function makePathString(path){ if(!path || path.length===0) return 'No path found'; return path.map(p=>{ const n=nodes.find(x=>x.id===p); return n? n.label : p}).join(' → ')}

function compareAndShow(start,goal){
  resultsDiv.innerHTML = '';
  const d = dijkstra(start,goal);
  const a = astar(start,goal);
  // show each result
  const pre = document.createElement('pre');
  pre.textContent = `Dijkstra | Path: ${makePathString(d.path)} | Cost: ${d.totalCost.toFixed?d.totalCost.toFixed(2):d.totalCost} | Nodes Explored: ${d.nodesExplored} | Time: ${formattedTime(d.executionTimeNs)}`;
  resultsDiv.appendChild(pre);
  const pre2 = document.createElement('pre');
  pre2.textContent = `A*       | Path: ${makePathString(a.path)} | Cost: ${a.totalCost.toFixed? a.totalCost.toFixed(2):a.totalCost} | Nodes Explored: ${a.nodesExplored} | Time: ${formattedTime(a.executionTimeNs)}`;
  resultsDiv.appendChild(pre2);

  // analysis
  const analysis = document.createElement('div');
  analysis.innerHTML = `<strong>Analysis:</strong><br>`;
  if(d.totalCost===a.totalCost) analysis.innerHTML += `✔ Both algorithms found the SAME optimal cost: ${d.totalCost.toFixed(2)}<br>`;
  else analysis.innerHTML += `⚠ Cost differs — Dijkstra: ${d.totalCost} | A*: ${a.totalCost}<br>`;
  const nodeDiff = d.nodesExplored - a.nodesExplored;
  if(nodeDiff>0) analysis.innerHTML += `✔ A* explored ${nodeDiff} fewer nodes (${a.nodesExplored} vs ${d.nodesExplored})<br>`;
  else if(nodeDiff<0) analysis.innerHTML += `! Dijkstra explored fewer nodes (${d.nodesExplored} vs ${a.nodesExplored}) — heuristic may not help<br>`;
  else analysis.innerHTML += `= Both explored the same number of nodes.<br>`;
  analysis.innerHTML += `Time — Dijkstra: ${formattedTime(d.executionTimeNs)} | A*: ${formattedTime(a.executionTimeNs)}<br>`;
  analysis.innerHTML += `<strong>Verdict:</strong> ${a.nodesExplored < d.nodesExplored ? 'A* is more EFFICIENT for this route.' : 'Both performed similarly.'}`;
  resultsDiv.appendChild(analysis);

  // highlight path (prefer A* path color and Dijkstra lines too)
  highlightPath(d.path);
}

function highlightPath(path){
  // reset
  drawGraph();
  if(!path || path.length<2) return;
  // highlight edges
  for(let i=0;i<path.length-1;i++){
    const a=path[i], b=path[i+1];
    // find the corresponding line by dataset
    const lines = Array.from(svg.querySelectorAll('line'));
    const match = lines.find(l=>{
      const d = l.dataset.edge;
      return d===`${a}-${b}` || d===`${b}-${a}`;
    });
    if(match) match.classList.add('path-edge');
    // highlight nodes
    const circle = svg.querySelector(`circle[data-id="${a}"]`);
    if(circle) circle.classList.add('path-node');
    const circle2 = svg.querySelector(`circle[data-id="${b}"]`);
    if(circle2) circle2.classList.add('path-node');
  }
}

function populateSelects(){
  // clear
  startSelect.innerHTML = '';
  goalSelect.innerHTML = '';
  nodes.forEach(n=>{
    const o1 = document.createElement('option'); o1.value=n.id; o1.textContent=n.label||n.id; startSelect.appendChild(o1);
    const o2 = document.createElement('option'); o2.value=n.id; o2.textContent=n.label||n.id; goalSelect.appendChild(o2);
  });
  // default to first and last
  if(nodes.length>0){ startSelect.value = nodes[0].id; goalSelect.value = nodes[nodes.length-1].id; }
}
// wire new controls
presetSelect.addEventListener('change', (e)=> setPreset(e.target.value));
runBothBtn.addEventListener('click', ()=>{
  const s = startSelect.value, g = goalSelect.value;
  compareAndShow(s,g);
});
resetBtn.addEventListener('click', ()=>{ drawGraph(); resultsDiv.innerHTML=''; });

// init
setPreset('minimal');
