package net.sf.cpsolver.ifs.assignment.context;

import net.sf.cpsolver.ifs.assignment.Assignment;
import net.sf.cpsolver.ifs.model.Model;
import net.sf.cpsolver.ifs.model.Value;
import net.sf.cpsolver.ifs.model.Variable;

/**
 * An interface holding all assignment contexts associated with one assignment. It also
 * creates a new assignment context when there is no assignment context associated with the given
 * reference.<br><br>
 * 
 * Method {@link AssignmentContextReference#getIndex()} can be used to index stored contexts. 
 * A new assignment context can be created for a reference by calling {@link HasAssignmentContext#createAssignmentContext(Assignment)}
 * on the {@link AssignmentContextReference#getParent()}.
 * 
 * @see AssignmentContext
 * @see AssignmentContextReference
 * @see HasAssignmentContext
 * 
 * @version IFS 1.2 (Iterative Forward Search)<br>
 *          Copyright (C) 2014 Tomas Muller<br>
 *          <a href="mailto:muller@unitime.org">muller@unitime.org</a><br>
 *          <a href="http://muller.unitime.org">http://muller.unitime.org</a><br>
 * <br>
 *          This library is free software; you can redistribute it and/or modify
 *          it under the terms of the GNU Lesser General Public License as
 *          published by the Free Software Foundation; either version 3 of the
 *          License, or (at your option) any later version. <br>
 * <br>
 *          This library is distributed in the hope that it will be useful, but
 *          WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *          Lesser General Public License for more details. <br>
 * <br>
 *          You should have received a copy of the GNU Lesser General Public
 *          License along with this library; if not see <http://www.gnu.org/licenses/>.
 **/
public interface AssignmentContextHolder<V extends Variable<V, T>, T extends Value<V, T>> {

    /**
     * Return assignment context for the given assignment and reference. A new assignment context is created
     * when there is not assignment context associated with the given reference yet.
     * @param assignment current assignment (there is only one assignment associated with one holder, but 
     * the assignment is passed so that {@link HasAssignmentContext#createAssignmentContext(Assignment)} can be called if needed
     * @param reference a reference created by calling {@link Model#createReference(HasAssignmentContext)} 
     * @return an assignment context
     */
    public <U extends AssignmentContext> U getAssignmentContext(Assignment<V, T> assignment, AssignmentContextReference<V, T, U> reference);

}