package net.sf.cpsolver.studentsct.heuristics;

import java.util.Enumeration;
import java.util.Vector;

import net.sf.cpsolver.ifs.heuristics.NeighbourSelection;
import net.sf.cpsolver.ifs.model.Neighbour;
import net.sf.cpsolver.ifs.solution.Solution;
import net.sf.cpsolver.ifs.solver.Solver;
import net.sf.cpsolver.ifs.util.DataProperties;
import net.sf.cpsolver.studentsct.StudentSectioningModel;
import net.sf.cpsolver.studentsct.model.Student;

/**
 * Two-phase (Batch) student sectioning neighbour selection.
 * It is based on {@link StudentSctNeighbourSelection}, however in the first round, only real students are sectioned.
 * All dummy students are removed from the problem during initialization of this neighbour selection, they 
 * are returned into the problem after the first round of {@link StudentSctNeighbourSelection}.
 * 
 * <br><br>
 * 
 * @version
 * StudentSct 1.1 (Student Sectioning)<br>
 * Copyright (C) 2007 Tomas Muller<br>
 * <a href="mailto:muller@unitime.org">muller@unitime.org</a><br>
 * Lazenska 391, 76314 Zlin, Czech Republic<br>
 * <br>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <br><br>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <br><br>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

public class TwoPhaseStudentSctNeighbourSelection extends StudentSctNeighbourSelection {
    private int iNrRounds = 7;

    public TwoPhaseStudentSctNeighbourSelection(DataProperties properties) throws Exception {
        super(properties);
        iNrRounds = properties.getPropertyInt("TwoPhaseSectioning.NrRoundsFirstPhase", iNrRounds);
    }
    
    /** Initialization -- also remove all the dummy students from the problem */
    public void init(Solver solver) {
        super.init(solver);
        if (removeDummyStudents(solver.currentSolution())) 
            registerSelection(new RestoreDummyStudents());
    }
    
    Vector iDummyStudents = null;
    private boolean removeDummyStudents(Solution solution) {
        StudentSectioningModel model = (StudentSectioningModel)solution.getModel();
        if (model.getNrLastLikeStudents(false)==0 || model.getNrRealStudents(false)==0) return false;
        iDummyStudents = new Vector();
        for (Enumeration e=new Vector(model.getStudents()).elements();e.hasMoreElements();) {
            Student student = (Student)e.nextElement();
            if (student.isDummy()) {
                iDummyStudents.add(student);
                model.removeStudent(student);
            }
        }
        return true;
    }

    private boolean addDummyStudents(Solution solution) {
        if (iDummyStudents==null || iDummyStudents.isEmpty()) return false;
        iNrRounds--;
        if (iNrRounds>0) return false;
        solution.restoreBest();
        StudentSectioningModel model = (StudentSectioningModel)solution.getModel();
        for (Enumeration e=iDummyStudents.elements();e.hasMoreElements();) {
            Student student = (Student)e.nextElement();
            model.addStudent(student);
        }
        iDummyStudents = null;
        solution.saveBest();
        return true;
    }

    /** Return all dummy students into the problem, executed as the last phase of the first round */
    protected class RestoreDummyStudents implements NeighbourSelection {
        public RestoreDummyStudents() {}
        public void init(Solver solver) {}
        /** Return all (removed) dummy students into the problem */
        public Neighbour selectNeighbour(Solution solution) {
            addDummyStudents(solution);
            return null;
        }
    }
}
