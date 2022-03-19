engine.name = 'Cold'

function init()
  isWindPlaying = false;
  windVol = 100.0; 
  windIntensity = 25.0;
  engine.startWind(0.0);
  updateParams();
end

function key(n,z)
    if n == 3 and z == 1 then
    isWindPlaying = not isWindPlaying
    updateParams();
    redraw();
  end
end

function enc(n,d)
  if n == 2 and isWindPlaying then
    windVol = bounded(windVol + d);
  end
  if n == 3 and isWindPlaying then
    windIntensity = bounded(windIntensity + d);
  end
  
  updateParams();
  redraw();
end

function updateParams()
    if isWindPlaying then
      engine.setWindVol(windVol/100.0);
      engine.setWindIntensity(windIntensity/100.0);
    else
      engine.setWindVol(0.0);
    end   
end

function bounded(x)
  if x < 0.0 then
    return 0.0
  elseif x > 100.0 then
    return 100.0
  else
    return x
  end
end

function redraw()
  screen.clear();
  screen.move(4,20);
  screen.text(isWindPlaying and "Winddddy~~~" or "---");
  screen.move(4,26);
  screen.text(isWindPlaying and "volume:   " .. windVol or "");
  screen.move(4,32);
  screen.text(isWindPlaying and "intensity: " .. windIntensity or "");
  screen.update();
end
