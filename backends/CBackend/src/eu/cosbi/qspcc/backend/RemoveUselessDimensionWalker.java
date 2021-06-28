package eu.cosbi.qspcc.backend;

import java.util.ArrayList;
import java.util.List;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.backend.attributes.CAttr;
import eu.cosbi.qspcc.dag.DAGFunctionalListener;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.MatrixType;

//@formatter:off
/**
 * removes useless dimension from colomn vector -> they'll become simple one-dimensional
 * array
 * 
 * @author giampiccolo
 *
 */
//@formatter:on
public class RemoveUselessDimensionWalker implements DAGFunctionalListener<AAST, AASTNode> {
	 
	@Override
	public void on(AAST aast, AASTNode node) throws GException {
			//check if the variable type is something like 
			GType type = node.expr();
			if(type!=null && type instanceof MatrixType){
				
				String matrixName = type.name();
				
				IntType[] dimensions = ((DimensionType) type).dims();
				
				if(dimensions!=null){
						
					Boolean aChangeIsGonnaCome = false;
					boolean[] survivedDimensions = new boolean[dimensions.length];
					
					//check if I have to delete a dimension (if it has an int value and it's one)
					List<IntType> dimensionsToKeep = new ArrayList<IntType>();
					for(int l=0; l<dimensions.length; l++){
							
						IntType dimension = dimensions[l];
							//check if it has an integer value and, just in case if it's one
						if(!(dimension.hasIntValue() && dimension.valueAsInt().equals(1))){
								
							survivedDimensions[l] = true;
								
							if(dimension.name().startsWith(matrixName+".dim")){								
								dimension.name(matrixName + ".dim"+(dimensionsToKeep.size()+1));
							}
							
							dimensionsToKeep.add(dimension);
							aChangeIsGonnaCome = true;
						}else{
							survivedDimensions[l] = false;
						}
					}
						
						//recreate the matrix type it it's necessary
					if(aChangeIsGonnaCome){
							
							//creates the new matrix type
						IntType[] dimensionsToKeepAsArray = dimensionsToKeep.toArray(new IntType[dimensionsToKeep.size()]);
						
						GType mType = null;
						mType = new MatrixType(type.type(), type.name(), ((DimensionType) type).of(), dimensionsToKeepAsArray);
							
						//put it on the node-type
						node.expr(mType, true);
						
						node.attr(CAttr.SURVIVED_DIMESIONS, survivedDimensions);
					}
				}	
			}
		}
}
