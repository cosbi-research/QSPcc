package eu.cosbi.qspcc.expressions.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.symbols.Symbols;
import eu.cosbi.utils.Triple;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

public class GType implements TypeDefinition {
    protected static Logger logger = LogManager.getLogger(GType.class);

    public static String STRUCT_SEP = ".";
    public static String SLICE_SEP = ":";
    public static String VECTOR_VAL_SEP = "$";
    public static String VECTOR_TYPE_SEP = "|";
    public static String FUN_PARAMS_SEP = "$";
    // real enum mainly for equals
    protected BType TYPE;

    protected String name = null;
    boolean isVirtual = false;
    // if this expr type is inside a structure, this will be non-null
    // and will point to the base structure ExprType
    private AASTNode baseStructNode = null;
    // params given while initializing
    protected Object[] params = null;
    // input symbol to use to access to this GType
    private String input = null;
    // this type is known, no need to create this type in target language
    protected boolean knownType = false;

    /**
     * copy constructor
     * 
     * @param o
     * @return
     */
    public static GType get(GType o) {
	GType instance = null;
	// get class that implements this type
	Class<? extends GType> clazz = o.type().type();
	try {
	    Constructor<? extends GType> constructor = clazz.getConstructor(GType.class);
	    instance = clazz.cast(constructor.newInstance(o));
	} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
		| InvocationTargetException | IllegalArgumentException e) {
	    logger.error("Missing type '" + clazz + "' : " + e.getMessage());
	    logger.debug("Missing type '" + clazz + "' : " + e.getMessage(), e);
	}
	return instance;
    }

    public static GType get(BType type) {
	return get(type, new Object[0]);
    }

    public static <T extends GType> T get(BType type, Class<T> clazz, Object... params) {
	T instance = null;
	try {
	    Constructor<T> constructor = clazz.getConstructor(BType.class, Object[].class);
	    instance = clazz.cast(constructor.newInstance(type, params));
	} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
		| InvocationTargetException | IllegalArgumentException e) {
	    logger.error("Missing type '" + clazz + "' : " + e.getMessage());
	    logger.debug("Missing type '" + clazz + "' : " + e.getMessage(), e);
	} catch (Exception e) {
	    logger.error("Error initializing type: " + type + " with params " + params + ": " + e.getMessage());
	    logger.debug("Error initializing type: " + type + " with params " + params + ": " + e.getMessage(), e);
	}
	return instance;
    }

    public Object[] params() {
	return this.params;
    }

    public static GType get(BType type, Object... params) {
	Class<? extends GType> clazz = type.type();
	return get(type, clazz, params);
    }

    private static Pattern baseTypePattern = Pattern.compile("^[A-Z]+$");
    private static Pattern matrixTypePattern = Pattern
	    .compile("^MATRIX\\[((?:\\s*(?:[0-9]+|INT)\\s*,?\\s*)+)]\\s+of\\s+([A-Z]+)$");
    private static Pattern matrixDimPattern = Pattern.compile("([0-9]+)");

    public static GType get(String name, String serializedType) throws TypeException {
	Matcher baseTypeMatcher = baseTypePattern.matcher(serializedType);
	if (baseTypeMatcher.find()) {
	    return get(BType.valueOf(baseTypeMatcher.group()));
	} else {
	    Matcher matrixTypeMatcher = matrixTypePattern.matcher(serializedType);
	    if (matrixTypeMatcher.find()) {
		String typesStr = matrixTypeMatcher.group(1);
		String ofStr = matrixTypeMatcher.group(2);
		GType of = GType.get(BType.valueOf(ofStr));
		String[] typesArr = typesStr.split(",");
		IntType[] dims = new IntType[typesArr.length];
		Matcher matrixDimMatcher = null;
		for (int i = 0; i < typesArr.length; ++i) {
		    String typeStr = typesArr[i];
		    matrixDimMatcher = matrixDimPattern.matcher(typeStr.replaceAll("\\s+", ""));
		    if (matrixDimMatcher.matches()) {
			// int with number 90
			dims[i] = (IntType) GType.get(BType.INT, Integer.parseInt(matrixDimMatcher.group(1)));
		    } else {
			// int without number INT
			dims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(name, i + 1));
		    }
		}

		return GType.get(BType.MATRIX, name, of, dims);
	    } else {
		throw new TypeException(ErrorMessage.USER_SYNTAX_ERROR_FUNCTION_TYPES_HEADER, null, serializedType);
	    }
	}

    }

    /**
     * is an INPUT type (no need for declaration)
     */
    public GType setInput(String inputName) {
	this.input = inputName;
	if (this instanceof StructType)
	    ((StructType) this).fullname(name);
	return this;
    };

    public boolean isInput() {
	return input != null;
    };

    public String inputName() {
	return input;
    }

    public GType setKnown() {
	this.knownType = true;
	if (this instanceof StructType)
	    ((StructType) this).fullname(name);
	return this;
    }

    public boolean isKnown() {
	return knownType;
    }

    /**
     * set that this is a type not linked to any node, just created by the
     * middle-end
     * 
     * @return
     */
    public GType setVirtual() {
	this.isVirtual = true;
	return this;
    }

    public boolean isVirtual() {
	return isVirtual;
    };

    public void baseStruct(AASTNode baseNode) {
	this.baseStructNode = baseNode;
    }

    public AASTNode baseStruct() {
	return this.baseStructNode;
    }

    @Override
    public boolean hasChild(Class<? extends GType> clazz) {
	return false;
    }

    @Override
    public GType child(Class<? extends GType> clazz) {
	return null;
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public GType(GType o) {
	this.TYPE = o.TYPE;
	this.input = o.input;
	this.knownType = o.knownType;
	this.name = o.name;
	this.params = o.params;
    }

    public GType(BType type, Object... params) {
	this.params = params;
	this.TYPE = type;
    }

    public GType name(String symbol) {
	this.name = symbol;
	return this;
    }

    /**
     * change name also of derivate symbols if present
     */
    public GType nameall(String symbol) {
	return name(symbol);
    }

    public boolean hasName() {
	return name != null;
    }

    public String name() {
	if (name == null)
	    name = Symbols.getSymbolFromType("", this.TYPE);

	return name;
    }

    public boolean hasValue() {
	return this instanceof ValuedType;
    }

    public boolean equalValue(GType newtype) {
	if (!(this instanceof ValuedType) || !(newtype instanceof ValuedType)) {
	    return false;
	}
	ValuedType<?> me = (ValuedType<?>) this;
	ValuedType<?> other = (ValuedType<?>) newtype;
	// assume string representation of equal values is equal
	return (me.hasValue() && other.hasValue() && me.valueAsString().equals(other.valueAsString()))
		|| (!me.hasValue() && !other.hasValue());
    }

    @Override
    public boolean equals(Object o) {
	if (o == null)
	    return false;
	if (o instanceof GType)
	    return this.TYPE.equals(((GType) o).TYPE);
	else if (o instanceof List) {
	    // at least one should match
	    for (GType t : (List<GType>) o)
		if (this.TYPE.equals(t.TYPE))
		    return true;
	    return false;
	} else if (o instanceof BType)
	    return this.TYPE.equals((BType) o);
	else
	    return false;
    }

    /**
     * 
     * @param t
     * @return returns true if all the sub-types are equal (not only the top-level
     *         type)
     */
    public boolean deeplyEqual(GType t) {
	if (t == null)
	    return false;
	if (!this.equals(t))
	    return false;
	switch (TYPE) {
	// BASE TYPES
	case SCALAR:
	case INT:
	case BOOL:
	case STRING:
	case UNKNOWN:
	case UNDEFINED:
	case VOID:
	    return true;
	case MATRIX_ACCESS_LAST:
	case MATRIX_ACCESS_ALL:
	    IndexType mitype = ((IndexType) this);
	    IndexType titype = ((IndexType) t);
	    if (mitype.of() != null && titype.of() == null || mitype.of() == null && titype.of() != null)
		return false;
	    if (mitype.dimension() != null && titype.dimension() == null
		    || mitype.dimension() == null && titype.dimension() != null)
		return false;

	    return (((mitype.of() == null && titype.of() == null) || mitype.of().equals(titype.of()))
		    && ((mitype.dimension() == null && titype.dimension() == null)
			    || mitype.dimension().deeplyEqual(titype.dimension())));
	case MATRIX_ACCESS_SLICE:
	    SliceType mstype = ((SliceType) this);
	    SliceType tstype = ((SliceType) t);
	    if (mstype.of() != null && tstype.of() == null || mstype.of() == null && tstype.of() != null)
		return false;
	    if (mstype.dim() != null && tstype.dim() == null || mstype.dim() == null && tstype.dim() != null)
		return false;

	    return (((mstype.of() == null && tstype.of() == null) || mstype.of().equals(tstype.of()))
		    && ((mstype.dim() == null && tstype.dim() == null) || mstype.dim().deeplyEqual(tstype.dim())));
	case VECTOR:
	case MATRIX:
	    GType of = ((MatrixType) this).of();
	    return this.dimsEqual(t).second() && of.deeplyEqual(((DimensionType) t).of());
	case STRUCT:
	    StructType stype = (StructType) this;
	    StructType otherstype = (StructType) t;
	    return stype.name().equals(otherstype.name());
	case FUNCTION:
	    FunctionType ftype = (FunctionType) this;
	    FunctionType otherftype = (FunctionType) t;
	    // inputs
	    if (ftype.inputs().size() != otherftype.inputs().size())
		return false;
	    Iterator<GType> ftypeit = ftype.inputs().iterator();
	    Iterator<GType> oftypeit = otherftype.inputs().iterator();
	    while (ftypeit.hasNext())
		if (!ftypeit.next().deeplyEqual(oftypeit.next()))
		    return false;

	    // outputs
	    if (ftype.outputs().size() != otherftype.outputs().size())
		return false;
	    ftypeit = ftype.outputs().iterator();
	    oftypeit = otherftype.outputs().iterator();
	    while (ftypeit.hasNext())
		if (!ftypeit.next().deeplyEqual(oftypeit.next()))
		    return false;

	    return true;
	case TYPE:
	    Type ttype = (Type) this;
	    Type otherttype = (Type) t;
	    return ttype.of().deeplyEqual(otherttype.of());
	default:
	    logger.error("Unhandled type for deep equality " + t);
	    return false;
	}
    }

    public Tuple<IntType[], Boolean> dimsEqual(GType t) {
	if (!(this instanceof DimensionType) || !(t instanceof DimensionType))
	    return new Tuple<IntType[], Boolean>(new IntType[0], false);
	IntType[] origThisdims = ((DimensionType) this).dims();
	IntType[] thisdims = TypeUtils.realDims(origThisdims);
	IntType[] origTdims = ((DimensionType) t).dims();
	IntType[] tdims = TypeUtils.realDims(origTdims);
	List<IntType> different = new ArrayList<IntType>();

	boolean dims_check = thisdims == null || tdims == null || thisdims.length == tdims.length;
	if (dims_check)
	    if (thisdims != null && tdims != null) {
		for (int i = 0; i < tdims.length; ++i)
		    if (thisdims[i].hasValue() && tdims[i].hasValue()) {
			if (!thisdims[i].valueAsInt().equals(tdims[i].valueAsInt())) {
			    dims_check &= false;
			    different.add(thisdims[i]);
			}
		    } else if (thisdims[i].hasValue() || tdims[i].hasValue()) {
			dims_check &= false;
			different.add(thisdims[i]);
		    }
	    } else if (thisdims == null && tdims == null) {
		dims_check = true;
	    } else
		dims_check = false;

	return new Tuple<IntType[], Boolean>(different.toArray(new IntType[different.size()]), dims_check);

    }

    @Override
    public String toString() {
	return typeName();
    }

    public String toDebugString() {
	switch (TYPE) {
	case SCALAR:
	case INT:
	case BOOL:
	case STRING:
	case UNKNOWN:
	case UNDEFINED:
	case VOID:
	case MATRIX_ACCESS_LAST:
	case MATRIX_ACCESS_ALL:
	    return ((input != null) ? "(input)" : "") + TYPE.toString() + "{" + name + "}";
	case MATRIX_ACCESS_SLICE:
	    SliceType sltype = (SliceType) this;
	    if (sltype.slices() != null)
		return ((input != null) ? "(input)" : "") + TYPE.toString() + "{" + name + "}" + "["
			+ sltype.slices().first() + ", " + sltype.slices().second() + ", " + sltype.slices().third()
			+ "]";
	    else
		return ((input != null) ? "(input)" : "") + TYPE.toString() + "{" + name + "}";
	case VECTOR:
	case MATRIX:
	    List<String> dimstr = new ArrayList<String>();
	    if (((MatrixType) this).dims() != null)
		for (IntType dim : ((MatrixType) this).dims())
		    dimstr.add((dim == null) ? null : dim.toDebugString());
	    return ((input != null) ? "(input)" : "") + TYPE.toString() + "{" + name + "}" + "["
		    + String.join(", ", dimstr) + "] of " + ((MatrixType) this).of().toDebugString();
	case FUNCTION:
	    List<String> params = new ArrayList<String>();
	    List<GType> inputs = ((FunctionType) this).inputs();
	    List<String> rets = new ArrayList<String>();
	    List<GType> outputs = ((FunctionType) this).outputs();
	    if (inputs != null)
		for (GType input : inputs)
		    params.add((input == null) ? null : input.toString());

	    if (outputs != null)
		for (GType output : outputs)
		    rets.add(output.toString());

	    return ((input != null) ? "(input)" : "") + "FUNCTION[ret=" + String.join(", ", rets) + " params="
		    + String.join(", ", params) + "]";
	case STRUCT:
	    List<String> fields = new ArrayList<String>();
	    Iterator<Tuple<List<GType>, String>> it = ((StructType) this).iterFields();
	    while (it.hasNext()) {
		Tuple<List<GType>, String> field = it.next();
		String[] str = new String[field.first().size()];
		int i = 0;
		for (GType t : field.first())
		    str[i++] = t.toString();
		fields.add(field.second() + ": " + ((field.first() == null) ? null : String.join(", ", str)));
	    }
	    return ((input != null) ? "(input)" : "") + "STRUCT[" + String.join(", ", fields) + "]";
	case TYPE:
	    Type t = (Type) this;
	    return "<type " + t.of().toString() + ">";
	default:
	    return "TO-BE-ADDED";
	}
    }

    public String toShortString() {
	switch (TYPE) {
	case FUNCTION:
	    return "f( \u2026 )";
	default:
	    return typeName();
	}
    }

    public String typeName() {
	switch (TYPE) {
	case BOOL:
	    return "Logical";
	case INT:
	    return "Int";
	case SCALAR:
	    return "Scalar";
	case UNKNOWN:
	    return "Undetermined";
	case STRING:
	    return "String";
	case UNDEFINED:
	    return "Unrecognized";
	case VOID:
	    return "Void";
	case MATRIX_ACCESS_LAST:
	case MATRIX_ACCESS_ALL:
	    return TYPE.toString();
	case MATRIX_ACCESS_SLICE:
	    SliceType sltype = (SliceType) this;
	    if (sltype.slices() != null)
		return "Slice[" + sltype.slices().first() + ", " + sltype.slices().second() + ", "
			+ sltype.slices().third() + "]";
	    else
		return "Slice";
	case VECTOR:
	case MATRIX:
	    List<String> dimstr = new ArrayList<String>();
	    if (((MatrixType) this).dims() != null)
		for (IntType dim : ((MatrixType) this).dims())
		    if (dim == null)
			dimstr.add(null);
		    else if (dim.hasIntValue())
			dimstr.add(Integer.toString(dim.valueAsInt()));
		    else
			dimstr.add("n");
	    GType oftype = ((MatrixType) this).of();
	    if (oftype != null)
		return oftype.toString() + "[" + String.join(" x ", dimstr) + "]";
	    else
		return GType.get(BType.UNKNOWN) + "[" + String.join(" x ", dimstr) + "]";

	case FUNCTION:
	    List<String> params = new ArrayList<String>();
	    List<GType> inputs = ((FunctionType) this).inputs();
	    List<String> rets = new ArrayList<String>();
	    List<GType> outputs = ((FunctionType) this).outputs();
	    if (inputs != null)
		for (GType input : inputs)
		    params.add((input == null) ? null : input.typeName());

	    if (outputs != null)
		for (GType output : outputs)
		    rets.add((output == null) ? null : output.typeName());

	    return "f( " + String.join(", ", params) + " ) -> " + String.join(", ", rets);
	case STRUCT:
	    List<String> fields = new ArrayList<String>();
	    Iterator<Tuple<List<GType>, String>> it = ((StructType) this).iterFields();
	    while (it.hasNext()) {
		Tuple<List<GType>, String> field = it.next();
		String start = "";
		String end = "";
		String[] types = null;
		if (field.first() != null && field.first().size() > 1) {
		    start = "[";
		    end = "]";
		    types = new String[field.first().size()];
		    int i = 0;
		    for (GType f : field.first())
			types[i++] = f.typeName();
		} else if (field.first() == null)
		    types = new String[0];
		else {
		    types = new String[1];
		    types[0] = field.first().get(0).typeName();
		}

		fields.add(field.second() + ": " + start + String.join(", ", types) + end);
	    }
	    return "Struct[" + String.join(", ", fields) + "]";
	case TYPE:
	    Type t = (Type) this;
	    return "<type " + t.of().typeName() + ">";
	default:
	    return "TO-BE-ADDED";
	}
    }

    public BType type() {
	return TYPE;
    }

    /**
     * check if the parameter actualptype can be used in place of this
     * 
     * @param actualptype
     * @return
     */
    public boolean lessGenericThan(GType actualptype) {
	switch (actualptype.type()) {
	case BOOL:
	    return isLessGenericThanBool();
	case INT:
	    return isLessGenericThanInt();
	case SCALAR:
	    return isLessGenericThanScalar();
	case MATRIX_ACCESS_ALL:
	    return TYPE.equals(BType.MATRIX_ACCESS_ALL);
	case MATRIX_ACCESS_LAST:
	    return TYPE.equals(BType.MATRIX_ACCESS_LAST);
	case VECTOR:
	    return TYPE.equals(BType.VECTOR);
	case MATRIX_ACCESS_SLICE:
	case MATRIX:
	    return isLessGenericThanMatrix((DimensionType) actualptype);
	// || ((MatrixType) this).of().lessGenericThan(((MatrixType) actualptype).of());
	case STRING:
	    return TYPE.equals(BType.STRING);
	case STRUCT:
	    if (!TYPE.equals(BType.STRUCT))
		return false;
	    StructType stype = (StructType) this;
	    StructType otherstype = (StructType) actualptype;
	    if (stype.numberOfFields() > otherstype.numberOfFields())
		return false;
	    boolean compatibleFields = compatibleStructFields(stype, otherstype);
	    return compatibleFields && name().equals(actualptype.name());
	case FUNCTION:
	    return TYPE.equals(BType.FUNCTION);
	case TYPE:
	    return TYPE.equals(BType.TYPE);
	case UNDEFINED:
	case UNKNOWN:
	    // unknown can become everything
	    return true;
	default:
	    logger.error("Unhandled type " + actualptype);
	    return false;
	}
    }

    private boolean compatibleStructFields(StructType stype, StructType otherstype) {
	Iterator<Tuple<List<GType>, String>> stypefields = stype.iterFields();
	while (stypefields.hasNext()) {
	    Tuple<List<GType>, String> myfield = stypefields.next();
	    Tuple<List<GType>, String> otherfield = null;
	    boolean found = false;
	    Iterator<Tuple<List<GType>, String>> otherstypefields = otherstype.iterFields();
	    while (otherstypefields.hasNext()) {
		otherfield = otherstypefields.next();
		if (myfield.second().equals(otherfield.second())) {
		    found = true;
		    break;
		}
	    }
	    if (!found)
		return false;
	    GType myfieldtype = ((LinkedList<GType>) myfield.first()).peekLast();
	    GType otherfieldtype = ((LinkedList<GType>) otherfield.first()).peekLast();
	    if (!myfieldtype.equals(BType.UNKNOWN) && !otherfieldtype.equals(BType.UNKNOWN)
		    && !myfieldtype.deeplyEqual(otherfieldtype))
		return false;
	}
	return true;
    }

    public boolean isLessGenericThanMatrix(DimensionType dtype) {
	if (dtype.of() == null)
	    return false;
	else if (isCastableToScalar()) {
	    return this.lessGenericThan(dtype.of());
	} else if (isCastableToMatrix()) {
	    if (((DimensionType) this).of() != null)
		return ((DimensionType) this).of().lessGenericThan(dtype.of());
	    else
		return true;
	} else
	    return false;
    }

    public boolean isLessGenericThanSlice() {
	return TYPE.equals(BType.MATRIX_ACCESS_SLICE) || TYPE.equals(BType.INT) || TYPE.equals(BType.SCALAR);
    }

    public boolean isLessGenericThanScalar() {
	return TYPE.equals(BType.BOOL) || TYPE.equals(BType.INT) || TYPE.equals(BType.SCALAR);
    }

    public boolean isLessGenericThanBool() {
	return TYPE.equals(BType.BOOL);
    }

    public boolean isLessGenericThanInt() {
	return TYPE.equals(BType.INT) || TYPE.equals(BType.BOOL);
    }

    public boolean isCastableToScalar() {
	return isCastableToScalar(TYPE);
    }

    public static boolean isCastableToScalar(BType t) {
	return t.equals(BType.INT) || t.equals(BType.BOOL) || t.equals(BType.SCALAR)
		|| t.equals(BType.MATRIX_ACCESS_LAST);
    }

    public boolean isSingleValued() {
	return TYPE.equals(BType.INT) || TYPE.equals(BType.BOOL) || TYPE.equals(BType.SCALAR)
		|| TYPE.equals(BType.MATRIX_ACCESS_LAST) || TYPE.equals(BType.STRING);
    }

    public boolean isCastableToBool() {
	return isCastableToBool(TYPE);
    }

    public static boolean isCastableToBool(BType t) {
	return t.equals(BType.INT) || t.equals(BType.BOOL);
    }

    public boolean isCastableToInt() {
	return isCastableToInt(TYPE);
    }

    public static boolean isCastableToInt(BType t) {
	return t.equals(BType.INT) || t.equals(BType.BOOL) || t.equals(BType.SCALAR)
		|| t.equals(BType.MATRIX_ACCESS_LAST);
    }

    public boolean isCastableToAccessIndex() {
	return TYPE.equals(BType.INT) || TYPE.equals(BType.BOOL) || TYPE.equals(BType.SCALAR)
		|| TYPE.equals(BType.MATRIX_ACCESS_LAST);
    }

    public boolean isCastableToSlice() {
	return TYPE.equals(BType.MATRIX_ACCESS_SLICE);
    }

    public boolean isCastableToMatrix() {
	return TYPE.equals(BType.MATRIX_ACCESS_SLICE) || TYPE.equals(BType.MATRIX) || TYPE.equals(BType.VECTOR);
    }

    public boolean isCastableToString() {
	return TYPE.equals(BType.STRING);
    }

    public boolean isCastableToStruct(GType actualptype) {
	if (!TYPE.equals(BType.STRUCT))
	    return false;
	StructType stype = (StructType) this;
	StructType otherstype = (StructType) actualptype;
	if (stype.numberOfFields() < otherstype.numberOfFields())
	    return false;
	boolean compatibleFields = compatibleStructFields(otherstype, stype);
	return compatibleFields && stype.name().equals(otherstype.name());
    }

    public GType castTo(BType type) throws ClassCastException {
	switch (type) {
	case BOOL:
	    if (!isCastableToBool())
		throw new ClassCastException("Cannot cast '" + this + "' to '" + type + "'");
	    if (TYPE.equals(BType.BOOL))
		return GType.get(this);
	    else {
		IntType itype = ((IntType) this);
		return GType.get(BType.BOOL, (itype.hasValue() && !itype.valueAsInt().equals(0)) ? true : false);
	    }
	case INT:
	    if (!isCastableToInt())
		throw new ClassCastException("Cannot cast '" + this + "' to '" + type + "'");
	    break;
	case MATRIX_ACCESS_LAST:
	    if (!isCastableToAccessIndex())
		throw new ClassCastException("Cannot cast '" + this + "' to '" + type + "'");
	    break;
	case MATRIX_ACCESS_SLICE:
	    if (!isCastableToSlice())
		throw new ClassCastException("Cannot cast '" + this + "' to '" + type + "'");
	    break;
	case MATRIX:
	    if (!isCastableToMatrix())
		throw new ClassCastException("Cannot cast '" + this + "' to '" + type + "'");
	    break;
	case SCALAR:
	    if (!isCastableToScalar())
		throw new ClassCastException("Cannot cast '" + this + "' to '" + type + "'");
	    break;
	case STRING:
	    if (!isCastableToString())
		throw new ClassCastException("Cannot cast '" + this + "' to '" + type + "'");
	    break;
	case STRUCT:
	    throw new ClassCastException("Cannot cast '" + this + "' to '" + type + "'");
	case UNKNOWN:
	    // unknown can become everything
	    break;
	default:
	    throw new ClassCastException("Cannot cast '" + this + "' to '" + type + "'");
	}
	return GType.get(type, params());
    }

    public boolean castableTo(GType actualptype) {
	switch (actualptype.type()) {
	case BOOL:
	    return isCastableToBool();
	case INT:
	    return isCastableToInt();
	case MATRIX_ACCESS_LAST:
	    return isCastableToAccessIndex();
	case MATRIX_ACCESS_SLICE:
	    return isCastableToSlice();
	case MATRIX:
	    return isCastableToMatrix();
	case SCALAR:
	    return isCastableToScalar();
	case STRING:
	    return isCastableToString();
	case STRUCT:
	    return isCastableToStruct(actualptype);
	case UNKNOWN:
	    // unknown can become everything
	    return true;
	default:
	    return false;
	}
    }

    /**
     * 
     * @param t
     * @return returns true if this type can store without loss of precision
     *         elements of type t
     */
    public boolean canRepresent(BType t) {
	GType of;
	switch (TYPE) {
	case BOOL:
	    return t.equals(BType.BOOL);
	case MATRIX_ACCESS_LAST:
	    return t.equals(BType.INT);
	case INT:
	    return t.equals(BType.BOOL) || t.equals(BType.INT) || t.equals(BType.MATRIX_ACCESS_LAST);
	case SCALAR:
	    return isCastableToScalar(t);
	case MATRIX_ACCESS_SLICE:
	    return t.equals(BType.MATRIX_ACCESS_SLICE);
	case MATRIX:
	    if (!t.equals(BType.MATRIX) && !t.equals(BType.MATRIX_ACCESS_SLICE))
		return false;
	    else
		return true;
	case STRUCT:
	    StructType stype = (StructType) this;
	    if (!t.equals(BType.STRUCT))
		return false;
	    else
		return true;
	default:
	    return false;
	}
    }

    /**
     * 
     * @param t
     * @return returns true if this type can store without loss of precision
     *         elements of type t
     */
    public boolean canRepresent(GType t) {
	GType of;
	switch (TYPE) {
	case STRING:
	    return t.equals(BType.STRING);
	case BOOL:
	    return t.equals(BType.BOOL);
	case MATRIX_ACCESS_LAST:
	    return t.equals(BType.INT);
	case INT:
	    return t.equals(BType.BOOL) || t.equals(BType.INT) || t.equals(BType.MATRIX_ACCESS_LAST);
	case SCALAR:
	    return t.isCastableToScalar();
	case MATRIX_ACCESS_SLICE:
	    of = ((SliceType) this).of();
	    return t.equals(BType.MATRIX_ACCESS_SLICE) && of.canRepresent(((SliceType) t).of());
	case VECTOR:
	case MATRIX:
	    of = ((MatrixType) this).of();
	    if (!t.isCastableToMatrix())
		return false;
	    IntType[] thisdims = TypeUtils.realDims(((MatrixType) this).dims());
	    IntType[] tdims = TypeUtils.realDims(((DimensionType) t).dims());
	    boolean dims_check = thisdims.length >= tdims.length;
	    if (dims_check)
		for (int i = 0; i < tdims.length; ++i)
		    if (thisdims[i].hasValue() && tdims[i].hasValue())
			dims_check &= thisdims[i].valueAsInt() >= tdims[i].valueAsInt();
		    else
			// this should be an unbound int, if it's not this cannot represent a
			// (possibily unbounded) target matrix
			dims_check &= !thisdims[i].hasValue();

	    return dims_check && of.canRepresent(((DimensionType) t).of());
	case STRUCT:
	    if (!t.type().equals(BType.STRUCT))
		return false;
	    StructType stype = (StructType) this;
	    StructType otherstype = (StructType) t;
	    boolean compatibleFields = compatibleStructFields(stype, otherstype);
	    return compatibleFields && stype.name().equals(otherstype.name());
	default:
	    return false;
	}
    }

    /**
     * 
     * @param t
     * @return returns true if this type can store without loss of precision
     *         elements of type t, but is not of type t. Ex. this = MATRIX OF INT, t
     *         = INT
     */
    public boolean canBeForcedToRepresent(GType t) {
	GType of;
	switch (TYPE) {
	case MATRIX:
	    of = ((MatrixType) this).of();
	    // an int is a matrix 1x1
	    if (t.isCastableToScalar())
		return of != null && of.canRepresent(t);
	    else if (t.isCastableToMatrix()) {
		GType tof = ((DimensionType) t).of();
		return of != null && of.canRepresent(tof);
	    }
	    return this.canRepresent(t);
	case MATRIX_ACCESS_SLICE:
	    of = ((SliceType) this).of();
	    // a scalar is a slice from scalar to scalar
	    return this.canRepresent(t) || t.isCastableToScalar() && of.canRepresent(t);
	default:
	    return this.canRepresent(t);
	}
    }

    /**
     * 
     * @param t
     * @return returns the general type that can hold this type and t type (@see
     *         canBeForcedToRepresent)
     * @throws Exception if cannot force
     */
    public GType forceTo(GType t) throws Exception {
	switch (TYPE) {
	case MATRIX:
	    MatrixType mtype = (MatrixType) this;
	    GType of = mtype.of();
	    // an int is a matrix 1x1
	    if (t.isCastableToScalar()) {
		IntType[] dims = mtype.dims();
		// clone dimensions to avoid references
		IntType[] newdims = new IntType[dims.length];
		for (int i = 0; i < dims.length; ++i)
		    newdims[i] = (IntType) GType.get(dims[i]);
		if (of == null || t.canRepresent(of))
		    return GType.get(BType.MATRIX, this.name(), GType.get(t), newdims);
		// this should never happen
		return GType.get(this);
	    } else if (t.isCastableToMatrix()) {
		DimensionType dttype = (DimensionType) t;
		GType tof = dttype.of();
		// decide global of type
		GType gof;
		if (of.canRepresent(tof))
		    gof = GType.get(of);
		else
		    gof = GType.get(tof);
		// decide dimensions
		IntType[] thisdims = TypeUtils.realDims(((MatrixType) this).dims());
		IntType[] tdims = TypeUtils.realDims(((DimensionType) t).dims());
		IntType[] gdims;
		IntType[] maxdims;
		int commonpart, max;
		if (thisdims.length >= tdims.length) {
		    gdims = new IntType[thisdims.length];
		    commonpart = tdims.length;
		    max = thisdims.length;
		    maxdims = thisdims;
		} else {
		    gdims = new IntType[tdims.length];
		    commonpart = thisdims.length;
		    max = tdims.length;
		    maxdims = tdims;
		}
		// copy the greater one from the common part
		int i = 0;
		for (; i < commonpart; ++i)
		    if (thisdims[i].hasIntValue() && tdims[i].hasIntValue())
			if (thisdims[i].valueAsInt() > tdims[i].valueAsInt())
			    gdims[i] = (IntType) GType.get(thisdims[i]);
			else
			    gdims[i] = (IntType) GType.get(tdims[i]);
		    // else chose the unbound one
		    else if (!thisdims[i].hasIntValue())
			gdims[i] = (IntType) GType.get(thisdims[i]);
		    else
			gdims[i] = (IntType) GType.get(tdims[i]);
		// if there is still something missing add it
		for (; i < max; ++i)
		    gdims[i] = (IntType) GType.get(maxdims[i]);

		return GType.get(BType.MATRIX, this.name(), gof, gdims);
	    }
	case MATRIX_ACCESS_SLICE:
	    // this slice can be assigned with a variety of values, remove pre-defined
	    // values if given,
	    // change of-type if needed.
	    SliceType stype = (SliceType) this;
	    if (this.canRepresent(t))
		return stype;
	    of = stype.of();
	    if (!of.canRepresent(t) && t.canRepresent(of))
		// change of type
		of = t;
	    // remove eventual value from slice
	    Triple<GType, GType, GType> newslice = new Triple<GType, GType, GType>(GType.get(of), GType.get(of),
		    GType.get(of));
	    return GType.get(BType.MATRIX_ACCESS_SLICE, this.name(), of, newslice, stype.dim());
	default:
	    throw new Exception();
	}
    }

    public boolean isEmptyMatrix() {
	switch (this.type()) {
	case MATRIX_ACCESS_SLICE:
	case MATRIX:
	    DimensionType dtype = (DimensionType) this;
	    // check for non-zero dims
	    for (IntType d : dtype.dims())
		if (!d.hasValue() || !d.valueAsInt().equals(0))
		    return false;
	    return true;
	default:
	    return false;
	}
    }

}
