engine.name = 'Cold'

function init()
  params:add_number("windVol","Wind Volume",0,100,50);
  params:add_number("windInt","Wind Intensity",0,100,25);
  params:add_binary("isWindEnabled","Wind Enabled","toggle",0);
  engine.startWind(0.0);
  updateParams();
end

function key(n,z)
    if n == 3 and z == 1 then
      params:set("isWindEnabled", 1 - params:get("isWindEnabled"));
      updateParams();
      redraw();
    elseif n == 2 and z == 1 then
      engine.playLowNote();
    end
end

function enc(n,d)
  if params:get("isWindEnabled")==1 then
    if     n == 2 then params:set("windVol",params:get("windVol")+d);
    elseif n == 3 then params:set("windInt", params:get("windInt") + d);
    end
  end
  updateParams();
  redraw();
end


--I think there's a built-in listener for arg changes, please use it
function updateParams()
    if params:get("isWindEnabled")==1 then
      engine.setWindVol(params:get("windVol")/100.0);
      engine.setWindIntensity(params:get("windInt")/100.0);
    else
      engine.setWindVol(0.0);
    end   
end

function redraw()
  screen.clear();
  screen.move(4,20);
  screen.text(params:get("isWindEnabled")==1 and "Winddddy~~~" or "---");
  screen.move(4,26);
  screen.text(params:get("isWindEnabled")==1 and "volume:   " .. params:get("windVol") or "");
  screen.move(4,32);
  screen.text(params:get("isWindEnabled")==1 and "intensity: " .. params:get("windInt") or "");
  screen.update();
end
