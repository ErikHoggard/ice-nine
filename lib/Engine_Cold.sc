Engine_Cold : CroneEngine {
  var params;
  alloc {
  
    SynthDef("Cold", {
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
      var reverb = GVerb.ar(in:filter,roomsize:200,revtime:2,damping:0.2,inputbw:0.2,spread:15,
        drylevel:0.3,earlyreflevel:0.7,taillevel:0.5,maxroomsize: 300,mul:1.0,add: 0);
    	var signal = Pan2.ar(reverb,0);
    	Out.ar(0,signal); 
    }).add;
  	
    SynthDef("Low", {
        arg volume = 0.2, hz = 65.41;

        var lfoMod = SinOsc.kr(2.4,0.0,1.5,0.0);

        var env = EnvGen.kr(Env.new(
  	      levels: [1,1,0.5,0],
	        times:  [2,1,4],
	        curve:  [3,3,3]
        ), doneAction:2);

        var osc = SinOsc.ar(hz+lfoMod,0.0,volume,0.0) * env;
        var mix = Mix.ar([osc]);
        var signal = Pan2.ar(mix,0);
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
      Synth.new("Wind", [\intensity, msg[1]] ++ params.getPairs)
    });
    this.addCommand("setWindVol","f",{arg msg;
      ~windSynth.set(\volume,msg[1]);
    });
    this.addCommand("setWindIntensity","f",{arg msg;
      ~windSynth.set(\intensity,msg[1]);
    });
    


    this.addCommand("playLowNote","f",{ arg msg;
      Synth.new("Low", [\hz, msg[1]] ++ params.getPairs) 
    });
  }
}
