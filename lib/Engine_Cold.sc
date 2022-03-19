Engine_Cold : CroneEngine {
  var params;
  alloc {
  
    SynthDef("Wind", {
      arg volume = 1.0, intensity = 0.25;
    	//modulate the filter
    	var lfoFilterMod = LFNoise2.kr(
    		freq:0.5*LFNoise1.kr(
    			//slowly randomly modulate the wind speed over time
    			freq:0.01,
    			mul:1.0,
    			add:1.1
    		),
    		mul:intensity,
    		add:intensity+1
    	);
    	//when filter is high (high wind speed) add random gusts
    	var lfoFastFilterMod = LFNoise2.kr(
    		freq:3.0,
    		mul:(0.8*LFNoise1.kr(
    			freq:1*lfoFilterMod,
    			mul:1.0,
    			add:0)
    		),
    		add:0.0
    	);
    	//lower volume at lower filter cuttoffs to simulate wind speed/intensity
    	var noise = PinkNoise.ar(mul: volume*(lfoFilterMod-1.0) );
    	var mix = Mix.ar([noise]);
    	var filter = MoogFF.ar(in:mix,freq:400*(lfoFilterMod+lfoFastFilterMod),gain:3);
    	var reverb = FreeVerb.ar(filter,mix:0.7,room:1.0,damp:0.2,mul:1.0,add:0);
    	var signal = Pan2.ar(reverb,0);
    	Out.ar(0,signal);
  	}).add;
  	
    params = Dictionary.newFrom([
      \volume, 1.0,
      \intensity, 0.25;
    ]);
    
    params.keysDo({ arg key;
      this.addCommand(key,"f",{arg msg;
        params[key] = msg[1];
      });
    });
    
    this.addCommand("startWind", "f", { arg msg;
      ~windSynth = Synth.new("Wind", [\intensity, msg[1]] ++ params.getPairs)
    });
    
    
    this.addCommand("setWindVol","f",{arg msg;
      ~windSynth.set(\volume,msg[1]);
    });
    this.addCommand("setWindIntensity","f",{arg msg;
      ~windSynth.set(\intensity,msg[1]);
    });
  }
}
