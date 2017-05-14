package com.crossover.code;

import com.crossover.exception.AnalyzerException;

public interface Analyzer<Param, Return> {

  Return analyze(Param param) throws AnalyzerException;
}