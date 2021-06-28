package eu.cosbi.qspcc.frontend.exceptions;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class ThrowableErrorListener extends BaseErrorListener {

	   public static final ThrowableErrorListener INSTANCE = new ThrowableErrorListener();

	   @Override
	   public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
	      throws ParseCancellationException {
		     
	         throw new ParseCancellationException("Frontend error at line " + line + ":" + charPositionInLine + "'"+e.getCtx().getText()+"': " + msg);
	      }
	}
