package eu.cosbi.utils;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizException;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

public class ProgramUtils {
    static Logger logger = LogManager.getLogger(ProgramUtils.class);
    static Graph general_graph;
    static Graph pgraph;

    public static void toSvg(Program p, Path basePath) throws Exception {
	general_graph = graph("program").directed();

	p.mainCompilationUnit().walkOnEnter((Program ref, AAST anode) -> {
	    File fpath = basePath.resolve(anode.name() + ".svg").toFile();
	    Node aastnode = node(anode.name()).with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0));
	    for (AAST child : anode.childs())
		aastnode = aastnode
			.link(node(child.name()).with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0)));

	    general_graph = general_graph.with(aastnode);
	    if (anode.parent() != null) {
		Node parentaastnode = node(anode.parent().name()).with(Shape.RECTANGLE, Style.FILLED,
			Color.hsv(.7, .3, 1.0));
		general_graph = general_graph.with(parentaastnode);
	    }

	    pgraph = graph(anode.name()).directed().with(aastnode);
	    // parse this AAST
	    anode.rootNode().walkOnEnter((AAST aref, AASTNode curNode) -> {
		if (curNode.type().equals(NodeType.LINECOMMENT) || curNode.type().equals(NodeType.NL)
			|| curNode.type().equals(NodeType.SEMI))
		    return;
		Node parentNNode;

		String nodeAsString = null;
		nodeAsString = curNode.toStringComplete();

		//if (curNode.id().equals("ID569"))
		//    System.out.println(curNode.expr());

		if (nodeAsString.length() > 10000)
		    nodeAsString = nodeAsString.substring(0, 5000) + "\r\n...\r\n"
			    + nodeAsString.substring(nodeAsString.length() - 5000 - 1, nodeAsString.length() - 1);
		Node curNNode = null;
		if (curNode.expr() == null)
		    curNNode = node(nodeAsString);
		else
		    curNNode = node(nodeAsString).with(Shape.CIRCLE, Style.FILLED, Color.hsv(.53, .75, 1.0));
		// .with(Label.html("<TABLE BORDER=\"0\" CELLBORDER=\"0\"
		// CELLSPACING=\"1\"><TR><TD>"
		// + curNode.toString().replace("\n", "<br />") +
		// "</TD></TR><TR><TD>"
		// + curNode.stringAttrs() + "</TD></TR></TABLE>"));

		if (curNode.type().equals(NodeType.PROGRAM)) {
		    parentNNode = node(aref.name());
		    // link with parent
		    parentNNode = parentNNode.link(curNNode);
		} else {
		    String curParentString = null;
		    curParentString = curNode.parent().toStringComplete();
		    //curParentString = curParentString.replace("\r\n", "");
		    parentNNode = node(curParentString);
		}
		// link with children
		for (AASTNode child : curNode.childs())
		    if (!child.type().equals(NodeType.LINECOMMENT) && !child.type().equals(NodeType.NL)
			    && !child.type().equals(NodeType.SEMI)) {

			String childStringComplete = null;
			childStringComplete = child.toStringComplete();

			curNNode = curNNode.link(node(childStringComplete));
		    }
		// curNNode = curNNode.link(childs.toArray(new
		// Node[childs.size()]));
		pgraph = pgraph.with(curNNode);
		pgraph = pgraph.with(parentNNode);
	    }, false);

	    Graphviz viz;
	    viz = Graphviz.fromGraph(pgraph);
	    try {
		viz.render(Format.SVG).toFile(fpath);
	    } catch (GraphvizException | IOException e) {
		logger.error("Failed to generate " + fpath.getAbsolutePath() + ". Skipping.");
		logger.debug("Failed to generate " + fpath.getAbsolutePath() + ". Skipping.", e);
	    }

	}, false);

	Graphviz viz;
	viz = Graphviz.fromGraph(general_graph);
	File fpath = basePath.resolve("program.svg").toFile();
	try {
	    viz.render(Format.SVG).toFile(fpath);
	} catch (GraphvizException e) {
	    logger.error("Failed to generate " + fpath.getAbsolutePath() + ". Skipping.");
	    logger.debug("Failed to generate " + fpath.getAbsolutePath() + ". Skipping.", e);
	}
    }
}
